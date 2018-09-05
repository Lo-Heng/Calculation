package com.busterlo.app.calculation;


import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lo_heng on 2018/5/28.
 */


public class CompareFragment extends Fragment {
    private final double PERMILE_DD = 2;
    private final double PERMIN_DD = 0.45;
    private final double LONGDIS_DD = 1.5;
    private final double FIRSTPRICE_TEXI1 = 12;//一类车 前2.5公里
    private final double FIRSTPRICE_TEXI2 = 10;//二类车 前2.5公里
    private final double PERMILE_TEXI1 = 3;
    private final double PERMILE_TEXI2 = 2.6;
    private final double PERMIN_TEXT = 1;
    private final double LONGDIS_TEXI = 0.3;//加收30%
    private final double PERMILE_WARMCAR1 = 0.8;//warmcar 便宜的每公里
    private final double PERMIN_WARMCAR1 = 0.15;//warmcar每分钟价格
    private final double PERMILE_WARMCAR2 = 1.2;//warmcar每分钟价格
    private final double PERMIN_WARMCAR2 = 0.15;//warmcar 2 每分钟价格
    private final double PERMILE_LEO = 0.99;
    private final double PERMIN_LEO = 0.15;//利澳出行每分钟的价格
    private  String DDRULES = "计价规则\n" +
            "基础费：8元。\n普通时段：2元/公里\n时长费：0.45元/分钟\n远途费（超出12公里）：1.5元/公里\n" +
            "每公里约需1.2分钟";
    private  String TEXIRULES = "计价规则\n起步价：前2.5公里，一类车：12元。二类车:10元。\n" +
            "基准运价：6时-23时，一类车：3元/公里，二类车：2.6元/公里\n" +
            "等候运价：0.6元/分钟(由于满1元跳一次，所以计算可能有误差)\n" +
            "远途费（超出12公里)：加收30%，即一类车：3.9元/公里，二类车：3.38元/公里\n每公里约需1.2分钟";
    private int texi_type ;
    private double miles_original;

    HTextView htvDD,htvTexi,htvWarmCar,htvLeo;
    Button btnCity;
    LinearLayout llDD,llTexi,detailDD,detailTexi,llParent;
    TextView tvRulesTexi,tvRulesDD;
    TextInputLayout tilMiles,tilTime;
    EditText etMiles,etTime;
    ImageView tip_down_dd,tip_down_texi;
    Spinner spinnerTexi;
    int rulesDD_h,rulesTexi_h; //DD规则TextView的高度
    CharSequence charMiles,charTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("计价助手");
//        android.support.v7.app.ActionBar actionbar=((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionbar.hide();

//        getActivity().getActionBar().hide();
//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_compare,container,false);

        htvDD = (HTextView)view.findViewById(R.id.htv_dd);
        htvTexi = (HTextView)view.findViewById(R.id.htv_texi);
        llDD = (LinearLayout)view.findViewById(R.id.ll_dd);
        llTexi = (LinearLayout)view.findViewById(R.id.ll_texi);
        llParent = (LinearLayout)view.findViewById(R.id.ll_parent) ;
        detailTexi = (LinearLayout) view.findViewById(R.id.detail_texi);
        detailDD = (LinearLayout) view.findViewById(R.id.detail_dd);
        tip_down_dd = (ImageView)view.findViewById(R.id.tip_down_dd);
        tip_down_texi = (ImageView)view.findViewById(R.id.tip_down_texi);
        spinnerTexi = (Spinner)view.findViewById(R.id.spinner_texi);
        etMiles = (EditText)view.findViewById(R.id.et_miles);
        etTime = (EditText) view.findViewById(R.id.et_time);
        tvRulesDD =(TextView)view.findViewById(R.id.tv_rules_dd);
        tvRulesTexi =(TextView)view.findViewById(R.id.tv_rules_texi);
        tilMiles = (TextInputLayout)view.findViewById(R.id.til_miles) ;
        tilTime = (TextInputLayout)view.findViewById(R.id.til_time);
        btnCity = (Button) view.findViewById(R.id.btn_city);
        htvWarmCar = (HTextView)view.findViewById(R.id.htv_warmcar);
        htvLeo = (HTextView)view.findViewById(R.id.htv_leo);
        //规则显示
        tvRulesDD.setText(ToDBC(DDRULES.replace("\\n","\n")));
        tvRulesTexi.setText(ToDBC(TEXIRULES.replace("\\n","\n")));
        //隐藏


        //测量规则LinearLayout块的高度
        detailDD.setVisibility(View.VISIBLE);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        detailDD.measure(w, h);
        rulesDD_h = detailDD.getMeasuredHeight();
        detailDD.setVisibility(View.GONE);
        //测量
        detailTexi.setVisibility(View.VISIBLE);
        w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        detailTexi.measure(w, h);
        rulesTexi_h = detailTexi.getMeasuredHeight();
        detailTexi.setVisibility(View.GONE);

        btnCity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_input_add);
                builder.setTitle("选择一个城市");
                //    指定下拉列表的显示数据
                final String[] cities = {"珠海", "珠海", "珠海", "珠海", "珠海","珠海", "珠海", "珠海","珠海", "珠海", "珠海", "珠海","珠海", "珠海", "珠海"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getActivity(), "选择的城市为：" + cities[which], Toast.LENGTH_SHORT).show();
                        btnCity.setText(cities[which]);
                    }
                });
//                builder.show();
                Intent intent = new Intent(getActivity(),MapActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 下拉列表适配器
         */
        List<String> list = new ArrayList<String>();
        list.add("一类车");
        list.add("二类车");
        list.add("出租车");
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                return v;
            }
            @Override
            public int getCount() {
                return super.getCount()-1; // you don't display last item. It is used as hint.
            }
        };
        adapter.setDropDownViewResource(R.layout.dropdown_style);

        spinnerTexi.setAdapter(adapter);
        spinnerTexi.setSelection(adapter.getCount()); //display hint
        spinnerTexi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                texi_type = position;
                CaculateTexi(charMiles,charTime);
            }
            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 点击滴滴的LinearLayout 展开操作
        llDD.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                HiddenAnimUtils.newInstance(getActivity(), detailDD, tip_down_dd, rulesDD_h).toggle();
            }
        });
        // 点击Texi的LinearLayout 展开操作
        llTexi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HiddenAnimUtils.newInstance(getActivity(), detailTexi, tip_down_texi, rulesTexi_h).toggle();
            }
        });
        //文字改变监听
        etMiles.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                charMiles = charSequence;
                if (checkInput(tilMiles,charSequence)) {
                    CaculateDD(charSequence);//可以加更多参数，如车型
                    CaculateTexi(charMiles, charTime);
                    CaculateWarmCar(charMiles, charTime);
                    CaculateLeo(charMiles,charTime);
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                charTime = charSequence;
                if (checkInput(tilTime,charSequence)){
//                    CaculateDD(charSequence);//可以加更多参数，如车型
                    CaculateTexi(charMiles,charTime);
                    CaculateWarmCar(charMiles,charTime);
                    CaculateLeo(charMiles,charTime);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }
    /**
     * @功能 计算出滴滴打车的价格k
     * @param str
     */
    private void CaculateDD(CharSequence str) {
        double number = 0 ,temp = 0;

        if(str.toString().isEmpty() == false) {
            number = Double.parseDouble(str.toString());
            temp = PERMILE_DD * number + PERMIN_DD * (number * 1.2);
            if (number > 12)
                temp += LONGDIS_DD * (number - 12);
//                    Log.d("compareFragment","after trans"+ number);
            number = ((int)(temp*100))/100.0;
        }
        else{
            number = 0;
        }
        htvDD.setAnimateType(HTextViewType.SCALE);
        htvDD.animateText(Double.toString(number)); // animate
    }

    /**
     * 检测输入
     * @param charSequence
     */
    private boolean checkInput(TextInputLayout viewTil,CharSequence charSequence){
        double number = 0;

        if(charSequence.toString().isEmpty() || charSequence == null){
            showError(viewTil,"输入不能为空");
            return false;
        }
        else{

            number = Double.parseDouble(charSequence.toString());
            if(String.valueOf((int)number).length() > 6) {
                showError(viewTil, "输入的整数不能超过6位");
                return false;
            }
            else{
                viewTil.setErrorEnabled(false);
            }
            return true;
        }

    }
    /**
     * 显示错误提示
     * @param textInputLayout
     * @param error
     */
    private void showError(TextInputLayout textInputLayout, String error){
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.shake);
        animation.setDuration(150);
        textInputLayout.startAnimation(animation);
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }
    /**
     * @功能 计算出出租车的价格
     * @param strMiles
     */
    protected void CaculateTexi(CharSequence strMiles,CharSequence strTime) {
        double price = 0, miles = 0, time = 0;
        if (strMiles != null && strTime != null) {
            if (!strMiles.toString().isEmpty() && !strTime.toString().isEmpty()) {
                time = Double.parseDouble(strTime.toString());
                miles_original = miles = Double.parseDouble(strMiles.toString());

                switch (texi_type) {
                    case 0:
                        if (miles <= 0) {
                            price = 0;
                        }
                        if (miles <= 2.5 && miles > 0)
                            price = FIRSTPRICE_TEXI1;
                        else if (miles < 12 && miles > 2.5) {
                            price = (miles - 2.5) * PERMILE_TEXI1 + PERMIN_TEXT * time + FIRSTPRICE_TEXI1;
                        } else if (miles >= 12) {
                            price = FIRSTPRICE_TEXI1 + (12 - 2.5) * PERMILE_TEXI1 + PERMIN_TEXT * time
                                    + (PERMILE_TEXI1 * (1 + LONGDIS_TEXI)) * (miles - 12);
                        }
                        break;
                    case 1:
                        if (miles <= 0) {
                            price = 0;
                        }
                        if (miles <= 2.5 && miles > 0)
                            price = FIRSTPRICE_TEXI2;
                        else if (miles < 12 && miles > 2.5) {
                            price = (miles - 2.5) * PERMILE_TEXI2 + PERMIN_TEXT * miles + FIRSTPRICE_TEXI2;
                        } else if (miles >= 12) {
                            price = FIRSTPRICE_TEXI2 + (12 - 2.5) * PERMILE_TEXI2 + PERMIN_TEXT * miles
                                    + (PERMILE_TEXI2 * (1 + LONGDIS_TEXI)) * (miles - 12);
                        }
                        break;
                    default:
                        break;
                }
            }
            price = (int) (price * 100) / 100.0;
            htvTexi.setAnimateType(HTextViewType.ANVIL);
            htvTexi.animateText(Double.toString(price)); // animate
        }
    }
    /**
     * @功能 计算warmcar的价格
     * @param
     */
    protected void CaculateWarmCar(CharSequence strMiles,CharSequence strTime) {
        double price = 0 ,miles = 0,time = 0;
        if(strMiles != null && strTime != null ) {
//原始的公里数
            if(!strMiles.toString().isEmpty() && !strTime.toString().isEmpty()) {
                miles_original = miles = Double.parseDouble(strMiles.toString());
                time = Double.parseDouble(strTime.toString());
                if (miles <= 0) {
                    price = 0;
                }
                switch (0) {
                    case 0:
                        price = PERMILE_WARMCAR1 * miles + time * PERMIN_WARMCAR1;
                        break;
                    case 1:
                        price = PERMILE_WARMCAR2 * miles + time * PERMIN_WARMCAR2;
                        break;
                    default:
                        break;
                }
            }
        }
        price = (int)(price*100)/100.0;
        htvWarmCar.setAnimateType(HTextViewType.ANVIL);
        htvWarmCar.animateText(Double.toString(price)); // animate
    }/**
     * @功能 计算Leo的价格
     * @param
     */
    protected void CaculateLeo(CharSequence strMiles,CharSequence strTime) {
        double price = 0 ,miles = 0,time = 0;
        if(strMiles != null && strTime != null ) {
//原始的公里数
            if(!strMiles.toString().isEmpty() && !strTime.toString().isEmpty()) {
                miles_original = miles = Double.parseDouble(strMiles.toString());
                time = Double.parseDouble(strTime.toString());
                if (miles <= 0) {
                    price = 0;
                }
                switch (0) {
                    case 0:
                        price = PERMILE_LEO * miles + time * PERMIN_LEO;
                        break;
                    default:
                        break;
                }
            }
        }
        price = (int)(price*100)/100.0;
        htvLeo.setAnimateType(HTextViewType.ANVIL);
        htvLeo.animateText(Double.toString(price)); // animate
    }

    /**
     * 半角转换为全角
     *
     * @param
     * @return
     */
    public static String ToDBC(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

}
