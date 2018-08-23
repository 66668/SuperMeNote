package com.superme.sjynote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * sjy 2018-0821
 * 自定义输入框说明：
 * 只处理 4/6/8/8+个输入框的情况，其他情况不适合
 */

public class TextInputView extends LinearLayout implements TextWatcher, View.OnKeyListener, View.OnFocusChangeListener {

    private Context mContext;
    private OnInputFinishListener onCodeFinishListener;
    private LinearLayout layout2, layout1, layout3;//封装三个布局
    private long endTime = 0;

    /**
     * 根据输入框数量 设置layout1内容的显示个数
     * 该值 只有 0,2，4，
     * showNumber=0，布局layout1不显示
     */
    private int layout1_ninner_Number;//

    /**
     * layout1子布局宽
     */
    private int layout1_ninner_width;

    /**
     * layout1子布局高
     */
    private int layout1_ninner_height;

    /**
     * 输入框数量
     * 根据需求，只处理了4，8，8+的样式
     */
    private int inputNumber;

    /**
     * 输入框类型
     */
    private VCInputType inputType;
    /**
     * 输入框的宽度
     */
    private int inputWidth;

    /**
     * 输入框的宽度
     */
    private int inputHeight;

    /**
     * 文字颜色
     */
    private int inputTextColor;

    /**
     * 文字大小
     */
    private float inputTextSize;

    /**
     * 输入框背景
     */
    private int inputBackground;

    /**
     * 光标颜色
     */
    private int inputCursor;

    //==============================初始化及布局构建==============================

    /**
     * 布局中初始化
     *
     * @param context
     * @param attrs
     */
    public TextInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        @SuppressLint({"Recycle", "CustomViewStyleable"})
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.textInputViewStyle);//获取自定义样式
        inputNumber = typedArray.getInteger(R.styleable.textInputViewStyle_inputNumber, 4);
        int mInputType = typedArray.getInt(R.styleable.textInputViewStyle_inputType, VCInputType.NUMBER.ordinal());
        this.inputType = VCInputType.values()[mInputType];
        inputWidth = typedArray.getDimensionPixelSize(R.styleable.textInputViewStyle_inputWidth, 92);
        inputHeight = typedArray.getDimensionPixelSize(R.styleable.textInputViewStyle_inputHeight, 120);
        inputTextColor = typedArray.getColor(R.styleable.textInputViewStyle_inputColor, Color.YELLOW);
        inputTextSize = typedArray.getDimensionPixelSize(R.styleable.textInputViewStyle_inputSize, 24);
        inputBackground = typedArray.getResourceId(R.styleable.textInputViewStyle_inputBackground, R.drawable.input_cursor_style);
        inputCursor = typedArray.getResourceId(R.styleable.textInputViewStyle_inputCursor, R.drawable.input_cursor_style);

        //释放资源
        typedArray.recycle();
        //构建布局
        initView();
    }

    /**
     * 构建布局
     */
    @SuppressLint("ResourceAsColor")
    private void initView() {
        //需要重新构建需要clear
        removeAllViews();
        //初始化三个父布局
        layout1 = new LinearLayout(mContext);
        layout2 = new LinearLayout(mContext);
        layout3 = new LinearLayout(mContext);

        //设置 layout1_ninner_Number
        if (inputNumber <= 4) {
            layout1_ninner_Number = 2;
        } else if (inputNumber > 4 && inputNumber <= 6) {
            layout1_ninner_Number = 0;
        } else if (inputNumber > 6 && inputNumber <= 8) {
            layout1_ninner_Number = 4;
        } else {
            layout1_ninner_Number = 0;
        }
        //父布局方向
        setOrientation(VERTICAL);

        initLayout1();
        initLayout2();
        initLayout3();
    }

    /**
     * 构建第一个布局:显示 楼号 单元 楼层 户
     */
    private void initLayout1() {

        if (layout1_ninner_Number == 2) {//四个输入框
            if (inputWidth == 92) {

                layout1_ninner_width = inputWidth * 2 + 20;//偏量纠正
                layout1_ninner_height = 68;
            } else {
                layout1_ninner_width = inputWidth * 2 + 14;//偏量纠正
                layout1_ninner_height = 48;
            }

            for (int i = 0; i < 2; i++) {
                TextView textView = new TextView(mContext);
                initTextView(textView, i);
            }
            addView(layout1);
        } else if (layout1_ninner_Number == 4) {
            if (inputWidth == 92) {

                layout1_ninner_width = inputWidth * 2 + 20;//偏量纠正
                layout1_ninner_height = 68;
            } else {
                layout1_ninner_width = inputWidth * 2 + 14;//偏量纠正
                layout1_ninner_height = 48;
            }
            for (int i = 0; i < 4; i++) {
                TextView textView = new TextView(mContext);
                initTextView(textView, i);
            }
            addView(layout1);
        } else {//0
            return;
        }

    }


    /**
     * 创建布局2，该布局是EditText输入框
     */
    private void initLayout2() {
        layout2.setOrientation(HORIZONTAL);
        for (int i = 0; i < inputNumber; i++) {
            EditText editText = new EditText(mContext);
            initEditText(editText, i);
            layout2.addView(editText);

            if (i == 0) { //设置第一个editText获取焦点
                editText.setFocusable(true);
            }
        }
        addView(layout2);
    }

    /**
     * 创建布局3，提示内容 居中显示
     */
    private void initLayout3() {
        if (layout1_ninner_Number == 0) {
            return;
        }
        layout3.setOrientation(HORIZONTAL);
        TextView textView = new TextView(mContext);

        //
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 48);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        //设置textView在布局的位置
        textView.setLayoutParams(layoutParams);
        //textView的设置
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(mContext.getString(R.string.inputTips));
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.inputTips_color));
        textView.setTextSize(12);

        //添加布局
        layout3.addView(textView);
        addView(layout3);
    }

    /**
     * 初始化layout1的子控件
     * 只在 92*120 64*84使用和的情况使用
     *
     * @param textView
     * @param i
     */
    private void initTextView(TextView textView, int i) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(layout1_ninner_width, layout1_ninner_height);
        if (i == 0) {//第一个设置为0
            layoutParams.leftMargin = 0;
        } else {
            if (inputWidth == 92) {
                layoutParams.leftMargin = 20;
            } else {
                layoutParams.leftMargin = 14;
            }
        }

        layoutParams.gravity = Gravity.CENTER;

        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        //初始化为白色字体
        textView.setTextColor(Color.WHITE);
        textView.setId(i);
        //两个布局
        if (layout1_ninner_Number == 2) {
            if (i == 0) {//楼层
                textView.setText(mContext.getString(R.string.floor_number));
            } else {//户
                textView.setText(mContext.getString(R.string.house_number));
            }
        } else {
            if (i == 0) {//楼号
                textView.setText(mContext.getString(R.string.build_number));
            } else if (i == 1) {//单元
                textView.setText(mContext.getString(R.string.cell_number));
            } else if (i == 2) {//楼层
                textView.setText(mContext.getString(R.string.floor_number));
            } else {//户
                textView.setText(mContext.getString(R.string.house_number));
            }
        }
        layout1.addView(textView);
    }

    /**
     * 第i个EditText设置
     *
     * @param editText
     * @param i
     */
    private void initEditText(EditText editText, int i) {
        int marginWidth = 20;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(inputWidth, inputHeight);

        if (i == 0) {//第一个设置为0
            layoutParams.leftMargin = 0;
        } else {
            if (inputWidth == 92) {
                layoutParams.leftMargin = 20;
            } else {
                layoutParams.leftMargin = 14;
            }
        }

        layoutParams.gravity = Gravity.CENTER;

        editText.setLayoutParams(layoutParams);
        editText.setGravity(Gravity.CENTER);
        editText.setId(i);
        editText.setCursorVisible(true);
        editText.setMaxEms(1);
        editText.setTextColor(inputTextColor);
        editText.setTextSize(inputTextSize);
        editText.setMaxLines(1);
        editText.setPadding(0, 0, 0, 0);
        editText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        //设置过滤
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        switch (inputType) {
            case NUMBER:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case NUMBERPASSWORD:
                editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            case TEXT:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case TEXTPASSWORD:
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            default:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        editText.setOnKeyListener(this);
        editText.setBackgroundResource(inputBackground);

        //修改光标的颜色（反射）
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, inputCursor);
        } catch (Exception ignored) {
        }
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
        editText.setOnKeyListener(this);
    }

    //==============================override==============================

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() != 0) {
            focus();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            focus();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            backFocus();
            backFoucsLayout1();
        }
        return false;
    }

    /**
     * 处理layout2子控件相关
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        int childCount = 0;
        if (layout1_ninner_Number == 0) {
            childCount = ((LinearLayout) getChildAt(0)).getChildCount();
        } else {
            childCount = ((LinearLayout) getChildAt(1)).getChildCount();
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }


    //==============================private 布局操作==============================


    /**
     * 处理layout2焦点相关，，layout1变色相关，否已编辑（已编辑和正在编辑，变色，回退的输入框正常色）
     */
    private void focus() {
        foucsLayout1();
        int count = getChildCountOf2();
        EditText editText;

        //有值
        for (int i = 0; i < count; i++) {
            //
            editText = getEditText(i);

            if (editText.getText().length() > 0) {//有值 变色
                editText.setSelected(true);
            }
            if (editText.getText().length() < 1) {//正在编辑 变色
                editText.setSelected(true);//setSelected（）在setCursorVisible（）上，不可以颠倒
                editText.setCursorVisible(true);
                editText.requestFocus();
                return;
            }
        }

        //如果最后一个输入框有字符，则返回结果
        EditText lastEditText = getEditText(inputNumber - 1);

        if (lastEditText.getText().length() > 0) {
            lastEditText.setCursorVisible(false);
            lastEditText.setSelected(true);
            getResult();
        } else {
            lastEditText.setSelected(false);
        }
    }

    /**
     * 监听 X号键，有值变色，没值不变色，正在编辑变色
     */
    private void backFocus() {

        long startTime = System.currentTimeMillis();
        EditText editText;
        //循环检测有字符的`editText`，把其置空，并获取焦点。
        for (int i = inputNumber - 1; i >= 0; i--) {
            editText = getEditText(i);
            if (editText.getText().length() >= 1 && startTime - endTime > 100) {
                editText.setText("");
                editText.setCursorVisible(true);
                editText.requestFocus();
                if (i == inputNumber - 1) {
                    getEditText(i).setSelected(true);
                } else {
                    getEditText(i).setSelected(true);
                    getEditText(i + 1).setSelected(false);
                }
                endTime = startTime;
                return;
            }
        }
    }

    /**
     * 输入完成，返回结果
     * 返回两种结果，一种是string [],包含01，另一种返回int[],string数组大小和inputNumber相同，int数组大小和inputNumber相同
     */
    private void getResult() {
        StringBuffer stringBuffer = new StringBuffer();
        EditText editText;

        for (int i = 0; i < inputNumber; i++) {
            editText = getEditText(i);
            stringBuffer.append(editText.getText());

        }

        if (onCodeFinishListener != null) {
            onCodeFinishListener.onComplete(stringBuffer.toString());
        }
    }

    //==============================layout1相关==============================

    /**
     * 输入时 layout1的变色
     * <p>
     * 监听焦点
     */
    private void foucsLayout1() {
        if (layout1_ninner_Number == 0) {
            return;
        }
        for (int i = 0; i < inputNumber; i++) {
            EditText editText = getEditText(i);
            if (editText.hasFocus()) {
                if (i % 2 == 0) {
                    int layout1_position = i / 2;
                    TextView textView = getTextView(layout1_position);
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.input_select_color));
                } else {
                    int layout1_position = (i - 1) / 2;
                    TextView textView = getTextView(layout1_position);
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.input_select_color));
                }
            }
        }
    }

    /**
     * 监听焦点,处理焦点的后一个位置
     */
    private void backFoucsLayout1() {
        if (layout1_ninner_Number == 0) {
            return;
        }
        for (int i = 0; i < inputNumber; i++) {
            EditText editText = getEditText(i);
            if (editText.hasFocus()) {
                if (i % 2 == 1) {
                    int layout1_position = (i + 1) / 2;
                    if (layout1_position < layout1_ninner_Number) {
                        TextView textView = getTextView(layout1_position);
                        textView.setTextColor(Color.WHITE);
                    }
                }
            }
        }
    }


    /**
     * 获取layout1子控件TextView
     * <p>
     *
     * @param position layout1的子控件位置
     * @return
     */
    private TextView getTextView(int position) {
        return (TextView) ((LinearLayout) getChildAt(0)).getChildAt(position);
    }
    //==============================layout2相关==============================

    /**
     * 获取layout2子控件EditText
     *
     * @return
     */
    private EditText getEditText(int position) {
        if (layout1_ninner_Number == 0) {
            return (EditText) ((LinearLayout) getChildAt(0)).getChildAt(position);
        } else {
            return (EditText) ((LinearLayout) getChildAt(1)).getChildAt(position);
        }
    }

    /**
     * 获取layout2子控件个数
     *
     * @return
     */
    private int getChildCountOf2() {
        if (layout1_ninner_Number == 0) {
            return ((LinearLayout) getChildAt(0)).getChildCount();
        } else {
            return ((LinearLayout) getChildAt(1)).getChildCount();
        }
    }


    //==============================外部监听==============================
    public interface OnInputFinishListener {
        void onComplete(String content);
    }

    public void setOnInputFinishListener(OnInputFinishListener onCodeFinishListener) {
        this.onCodeFinishListener = onCodeFinishListener;
    }

    //==============================enum==============================

    /**
     * 输入样式：数字 文字 数字密码 密码
     */
    public enum VCInputType {
        NUMBER,
        TEXT,
        NUMBERPASSWORD,
        TEXTPASSWORD,
    }

    //==============================public 操作(和嵌入式相关，由外部键盘控制)==============================


    /**
     * 代码输入值
     * 输入字大于inputNumber，不处理
     *
     * @param string
     */

    public boolean setText(String string) {
        int lenght = string.length();
        char[] array = string.toCharArray();
        if (lenght == inputNumber) {
            EditText editText = null;
            for (int i = 0; i < inputNumber; i++) {
                editText = getEditText(i);
                editText.setText(array[i] + "");
                editText.setSelected(true);
            }
            //设置焦点
            focus();

            return true;
        } else if (lenght < inputNumber) {
            EditText editText = null;
            for (int i = 0; i < lenght; i++) {
                editText = getEditText(i);
                editText.setText(array[i] + "");
                editText.setSelected(true);
            }
            //设置焦点
            focus();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取值
     *
     * @return
     */

    public String getText() {
        EditText editText = null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < inputNumber; i++) {
            editText = getEditText(i);
            builder.append(editText.getText().toString());
        }
        return builder.toString();
    }

    //==============================属性getter setter==============================
    public int getInputNumber() {
        return inputNumber;
    }

    public void setInputNumber(int inputNumber) {
        this.inputNumber = inputNumber;
        synchronized (Thread.currentThread()) {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public VCInputType getInputType() {
        return inputType;
    }

    public void setInputType(VCInputType inputType) {
        this.inputType = inputType;
        synchronized (Thread.currentThread()) {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getInputWidth() {
        return inputWidth;
    }

    public void setInputWidth(int inputWidth) {
        this.inputWidth = inputWidth;
        synchronized (Thread.currentThread()) {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getInputHeight() {
        return inputHeight;
    }

    public void setInputHeight(int inputHeight) {
        this.inputHeight = inputHeight;
        synchronized (Thread.currentThread()) {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public int getInputTextColor() {
        return inputTextColor;
    }

    public void setInputTextColor(int inputTextColor) {
        this.inputTextColor = inputTextColor;
        synchronized (Thread.currentThread()) {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public float getInputTextSize() {
        return inputTextSize;
    }

    public void setInputTextSize(float inputTextSize) {
        this.inputTextSize = inputTextSize;
        synchronized (Thread.currentThread()) {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getInputBackground() {
        return inputBackground;
    }

    public void setInputBackground(int inputBackground) {
        this.inputBackground = inputBackground;
        synchronized (Thread.currentThread()) {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getInputCursor() {
        return inputCursor;
    }

    public void setInputCursor(int inputCursor) {
        this.inputCursor = inputCursor;
        synchronized (Thread.currentThread()) {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
