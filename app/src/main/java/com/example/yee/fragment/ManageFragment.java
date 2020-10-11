package com.example.yee.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.yee.MainActivity;
import com.example.yee.R;
import com.example.yee.dao.BillDao;
import com.example.yee.entity.Bill;
import com.example.yee.entity.User;
import com.example.yee.utils.ChartUtil;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 理财页面
 */
public class ManageFragment extends Fragment{

    //折线图
    private LineChartView lineChart;
    private TextView btn_out;
    private TextView btn_in;
    //统计类型
    private int typeOI; //收支类型
    private static final int OUT = 1;
    private static final int IN = 2;
    //账单数据
    private BillDao billDao;
    private List<Bill> billList;
    private User user;
    //总结
    private TextView out_money;
    private TextView in_money;
    private TextView lave_money;
    private TextView tip;

    String[] date = {"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"}; //X轴的坐标点
    double[] money;  //折线图的数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();  //数据点
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();  //x轴的坐标点

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage,container, false);
        //初始化折线图
        lineChart = (LineChartView) view.findViewById(R.id.line_chart);
        btn_out = view.findViewById(R.id.btn_out); //初始化控件
        btn_in = view.findViewById(R.id.btn_in);
        billDao = new BillDao(getContext());
        user = ((MainActivity)getActivity()).user;
        out_money = view.findViewById(R.id.out_money);
        in_money = view.findViewById(R.id.in_money);
        lave_money = view.findViewById(R.id.lave_money);
        tip = view.findViewById(R.id.tip);
        //默认显示支出
        typeOI = OUT;
        //按钮点击事件
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOI = OUT;
                btn_in.setBackgroundDrawable(getResources().getDrawable(R.drawable.income_button_normal));
                btn_in.setTextColor(getResources().getColor(R.color.btn_normal));
                btn_out.setBackgroundDrawable(getResources().getDrawable(R.drawable.outlay_button_selected));
                btn_out.setTextColor(getResources().getColor(R.color.btn_selected));
                initData();
            }
        });
        btn_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOI = IN;
                btn_out.setBackgroundDrawable(getResources().getDrawable(R.drawable.outlay_button_normal));
                btn_out.setTextColor(getResources().getColor(R.color.btn_normal));
                btn_in.setBackgroundDrawable(getResources().getDrawable(R.drawable.income_button_selected));
                btn_in.setTextColor(getResources().getColor(R.color.btn_selected));
                initData();
            }
        });
        initData(); //加载数据
        return view;
    }

    //初始化数据
    private void initData(){
        if(typeOI == OUT){
            billList = billDao.queryBill(user.getUsername(),"支出");
        }else{
            billList = billDao.queryBill(user.getUsername(),"收入");
        }
        money = ChartUtil.ChartEveryMonth(billList); //获取坐标点数据
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取折线图上的坐标点
        initLineChart();//初始化折线图
        //年度分析总结
        //支出
        double[] o_money = ChartUtil.ChartEveryMonth(billDao.queryBill(user.getUsername(),"支出"));
        for (int i=1; i<o_money.length; i++){
            o_money[0] += o_money[i];
        }
        out_money.setText("年总支出："+new DecimalFormat("#0.00").format(o_money[0])+"元");
        //收入
        double[] i_money = ChartUtil.ChartEveryMonth(billDao.queryBill(user.getUsername(),"收入"));
        for (int i=1; i<i_money.length; i++){
            i_money[0] += i_money[i];
        }
        in_money.setText("年总收入："+new DecimalFormat("#0.00").format(i_money[0])+"元");
        String res = new DecimalFormat("#0.00").format((i_money[0]-o_money[0]));
        if(i_money[0]-o_money[0]>0){
            lave_money.setText("收支盈余：+"+res+"元");
            tip.setText("闲钱理财，收益翻番~");
        }else{
            lave_money.setText("收支盈余："+res+"元");
            tip.setText("合理支出，稳健理财~");
        }
    }

    //设置x轴的显示
    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }
    //设置坐标点
    private void getAxisPoints() {
        mPointValues.clear(); //先清空原来的值在加载新的值
        for (int i = 0; i < money.length; i++) {
            mPointValues.add(new PointValue(i, Math.round(money[i])));
        }
    }

    //初始化折线图
    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#F26267EA"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);  //折线图上每个数据点的形状  圆形
        line.setCubic(true); //曲线是否平滑，即是曲线还是折线
        line.setFilled(true); //是否填充曲线的面积
        line.setHasLabels(true);  //曲线的数据坐标是否加上备注
        line.setHasLines(true); //是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true); //是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        //坐标轴x轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setTextSize(12);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(false); //x 轴分割线
        //坐标轴y轴
        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
//        Axis axisY = new Axis();  //Y轴
//        axisY.setName("");//y轴标注
//        axisY.setTextSize(10);//设置字体大小
//        axisY.setTextColor(Color.BLACK);
//        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        //x轴的固定个数
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 12;
        lineChart.setCurrentViewport(v);
    }

    //重现加载页面时
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initData(); //重新加载数据
        }
    }
}
