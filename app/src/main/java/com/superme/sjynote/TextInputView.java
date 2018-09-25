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
 *
 * sjy 2018-0821
 */

public class TextInputView extends LinearLayout implements TextWatcher, View.OnKeyListener, View.OnFocusChangeListener {

    private Context mContext;
    private OnInputFinishListener onCodeFinishListener;
    private LinearLayout layout2;
    private long endTime = 0;

    /**
     * 输入框数量
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


    /**
     * 布局3是否显示
     */
    private boolean islayout3show = true;


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
        //初始化布局
        layout2 = new LinearLayout(mContext);
        initLayout();
    }

    /**
     * 创建布局2，该布局是EditText输入框
     */
    private void initLayout() {
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
            if (inputWidth == 120) {
                layoutParams.leftMargin = 30;
            } else {
                layoutParams.leftMargin = 20;
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
        childCount = ((LinearLayout) getChildAt(0)).getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }


    //==============================private 布局操作==============================


    /**
     * (软键盘)
     * 处理layout2焦点相关，，layout1变色相关，否已编辑（已编辑和正在编辑，变色，回退的输入框正常色）
     */
    private void focus() {
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
     * (嵌入式)
     * 处理layout2焦点相关
     */
    private void focus1() {
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

    //==============================layout2相关==============================

    /**
     * 获取layout2子控件EditText
     *
     * @return
     */
    private EditText getEditText(int position) {
        return (EditText) ((LinearLayout) getChildAt(0)).getChildAt(position);

    }

    /**
     * 获取layout2子控件个数
     *
     * @return
     */
    private int getChildCountOf2() {
        return ((LinearLayout) getChildAt(0)).getChildCount();
    }


    //==============================外部监听(嵌入式不适用)==============================
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
     * 输入值
     * 输入字大于inputNumber，不处理
     *
     * @param string
     */

    public boolean setText(CharSequence string) {
        int lenght = string.length();
        char[] array = string.toString().toCharArray();
        if (lenght >= inputNumber) {
            EditText editText = null;
            for (int i = 0; i < inputNumber; i++) {
                editText = getEditText(i);
                editText.setText(array[i] + "");
                editText.setSelected(true);
            }
            //设置焦点
            focus1();
            return true;
        } else {//lenght < inputNumber
            EditText editText = null;
            //输入框变色

            for (int i = 0; i < inputNumber; i++) {
                editText = getEditText(i);
                if (i < lenght) {
                    editText.setText(array[i] + "");
                    editText.setSelected(true);

                } else {
                    editText.setText("");
                    editText.setSelected(false);
                }

            }
            //设置焦点
            focus1();
            return true;
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

    /**
     * 所有set设置完成之后，都要调用build
     */
    public void build() {
        synchronized (Thread.currentThread()) {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getInputNumber() {
        return inputNumber;
    }


    public TextInputView setInputNumber(int inputNumber) {
        this.inputNumber = inputNumber;
        return this;
    }

    public VCInputType getInputType() {
        return inputType;
    }

    public TextInputView setInputType(VCInputType inputType) {
        this.inputType = inputType;
        return this;
    }

    public int getInputWidth() {
        return inputWidth;
    }

    public TextInputView setInputWidth(int inputWidth) {
        this.inputWidth = inputWidth;
        return this;
    }

    public int getInputHeight() {
        return inputHeight;
    }

    public TextInputView setInputHeight(int inputHeight) {
        this.inputHeight = inputHeight;
        return this;
    }


    public int getInputTextColor() {
        return inputTextColor;
    }

    public TextInputView setInputTextColor(int inputTextColor) {
        this.inputTextColor = inputTextColor;
        return this;
    }

    public float getInputTextSize() {
        return inputTextSize;
    }

    public TextInputView setInputTextSize(float inputTextSize) {
        this.inputTextSize = inputTextSize;
        return this;
    }

    public int getInputBackground() {
        return inputBackground;
    }

    public TextInputView setInputBackground(int inputBackground) {
        this.inputBackground = inputBackground;
        return this;
    }

    public int getInputCursor() {
        return inputCursor;
    }

    public TextInputView setInputCursor(int inputCursor) {
        this.inputCursor = inputCursor;
        return this;
    }

    public TextInputView showLayout3Tips() {
        this.islayout3show = true;
        return this;
    }

    public TextInputView hideLayout3Tips() {
        this.islayout3show = false;
        return this;
    }


}
