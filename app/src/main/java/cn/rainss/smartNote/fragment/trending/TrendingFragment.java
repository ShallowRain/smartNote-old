package cn.rainss.smartNote.fragment.trending;

import android.os.Build;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rainss.smartNote.R;
import cn.rainss.smartNote.core.webview.BaseWebViewFragment;
import cn.rainss.smartNote.diary.DiaryApplication;
import cn.rainss.smartNote.diary.dao.DBManager;
import cn.rainss.smartNote.utils.Utils;

/**
 *
 * @since 2019-10-30 00:19
 */
@Page(anim = CoreAnim.none)
public class TrendingFragment extends BaseWebViewFragment {

    private DBManager mgr;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    private ChartInterface mChartInterface;
    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_trending;
    }

    @Override
    protected void initViews() {
        //初始化数据库类
        mgr = DBManager.getMgr(getActivity());
        //目前Echarts-Java只支持3.x
        mAgentWeb = Utils.createAgentWeb(this, flContainer, "file:///android_asset/chart/src/template.html");

        //注入接口,供JS调用
        mAgentWeb.getJsInterfaceHolder().addJavaObject("Android", mChartInterface = new ChartInterface());

    }

    @SingleClick
    @OnClick({R.id.btn_bar_chart, R.id.btn_line_chart, R.id.btn_pie_chart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bar_chart:
                initBarChart();
                break;
            case R.id.btn_line_chart:
                initLineChart();
                break;
            case R.id.btn_pie_chart:
                initPieChart();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initBarChart() {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makeBarChartOptions());
    }

    private void initLineChart() {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makeLineChartOptions());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initPieChart() {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makePieChartOptions());
    }

    /**
     * 注入到JS里的对象接口
     */
    public class ChartInterface {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public String makeBarChartOptions() {
            GsonOption option = new GsonOption();
            option.setTitle(new Title().text("天气统计"));
            option.setLegend(new Legend().data("天数"));
            Map<Integer, Integer> weather = mgr.getWeather();
            ArrayList<String> x = new ArrayList<>();
            ArrayList<Integer> y = new ArrayList<>();
            //遍历集合，处理数据
            weather.forEach((k,v)->{
                switch (k){
                    case R.drawable.weather1:
                        x.add("太阳");
                        y.add(v);
                        break;
                    case R.drawable.weather2:
                        x.add("晴");
                        y.add(v);
                        break;
                    case R.drawable.weather3:
                        x.add("多云");
                        y.add(v);
                        break;
                    case R.drawable.weather4:
                        x.add("阴");
                        y.add(v);
                        break;
                    case R.drawable.weather5:
                        x.add("雾");
                        y.add(v);
                        break;
                    case R.drawable.weather6:
                        x.add("小雨");
                        y.add(v);
                        break;
                    case R.drawable.weather7:
                        x.add("大雨");
                        y.add(v);
                        break;
                    case R.drawable.weather8:
                        x.add("雷阵雨");
                        y.add(v);
                        break;
                    case R.drawable.weather9:
                        x.add("雪");
                        y.add(v);
                        break;
                    case R.drawable.weather10:
                        x.add("冰雹");
                        y.add(v);
                        break;
                    case R.drawable.weather11:
                        x.add("晴转多云");
                        y.add(v);
                        break;
                    case R.drawable.weather12:
                        x.add("阴转小雨");
                        y.add(v);
                        break;
                }
            });

            option.xAxis(new CategoryAxis().data(x.toArray()));
            option.yAxis();
            Bar bar = new Bar("心情");
            bar.data(y.toArray());
            option.series(bar);
            return option.toString();
        }

        @JavascriptInterface
        public String makeLineChartOptions() {
            GsonOption option = new GsonOption();
            option.legend("高度(km)与气温(°C)变化关系");
            option.toolbox().show(false);
            option.calculable(true);
            option.tooltip().trigger(Trigger.axis).formatter("Temperature : <br/>{b}km : {c}°C");

            ValueAxis valueAxis = new ValueAxis();
            valueAxis.axisLabel().formatter("{value} °C");
            option.xAxis(valueAxis);

            CategoryAxis categoryAxis = new CategoryAxis();
            categoryAxis.axisLine().onZero(false);
            categoryAxis.axisLabel().formatter("{value} km");
            categoryAxis.boundaryGap(false);
            categoryAxis.data(0, 10, 20, 30, 40, 50, 60, 70, 80);
            option.yAxis(categoryAxis);

            Line line = new Line();
            line.smooth(true).name("高度(km)与气温(°C)变化关系").data(15, -50, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5).itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
            option.series(line);
            return option.toString();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public String makePieChartOptions() {
            Map<Integer, Integer> mood = mgr.getMood();
            AtomicInteger atomic = new AtomicInteger(0);
            //遍历数据
            PieData happy = new PieData("开心", 0);
            PieData sadness = new PieData("悲伤", 0);
            PieData anger = new PieData("愤怒", 0);
            PieData surprised = new PieData("惊讶", 0);
            PieData fear = new PieData("恐惧", 0);
            ArrayList<PieData> pieData = new ArrayList<>();
            ArrayList<String> legend = new ArrayList<>();
            mood.forEach((k,v)->{
                //开心、悲伤、愤怒、惊讶、恐惧
                switch(k){
                    case R.drawable.vector_drawable_mood2:
                    case R.drawable.vector_drawable_mood3:
                    case R.drawable.vector_drawable_mood5:
                    case R.drawable.vector_drawable_mood10:
                        happy.setValue((int) happy.getValue() + v);
                        break;
                    case R.drawable.vector_drawable_mood4:
                    case R.drawable.vector_drawable_mood12:
                        sadness.setValue((int) sadness.getValue() + v);
                        break;
                    case R.drawable.vector_drawable_mood11:
                        anger.setValue((int) anger.getValue() + v);
                        break;
                    case R.drawable.vector_drawable_mood1:
                    case R.drawable.vector_drawable_mood7:
                    case R.drawable.vector_drawable_mood9:
                    case R.drawable.vector_drawable_mood6:
                        surprised.setValue((int) surprised.getValue() + v);
                        break;
                    case R.drawable.vector_drawable_mood8:
                        fear.setValue((int) fear.getValue() + v);
                        break;

                }
            });
            //处理数据
            if((int)happy.getValue() != 0){
                pieData.add(happy);
                legend.add(happy.getName());
            }
            if((int)sadness.getValue() != 0){
                pieData.add(sadness);
                legend.add(sadness.getName());
            }
            if((int)anger.getValue() != 0){
                pieData.add(anger);
                legend.add(anger.getName());
            }
            if((int)surprised.getValue() != 0){
                pieData.add(surprised);
                legend.add(surprised.getName());
            }
            if((int)fear.getValue() != 0){
                pieData.add(fear);
                legend.add(fear.getName());
            }
            GsonOption option = new GsonOption();
            option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} 天({d}%)");
            option.legend().data(legend.toArray());
            option.setTitle(new Title().text("心情统计"));
            Pie pie = new Pie("心情状况").data(pieData.toArray())
                    .center("50%", "45%").radius("50%");
            pie.label().normal().show(true).formatter("{b}{c}({d}%)");
            option.series(pie);
            return option.toString();
        }
    }
}
