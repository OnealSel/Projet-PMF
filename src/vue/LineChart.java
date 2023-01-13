package vue;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;

public class LineChart extends ApplicationFrame {

    public JFreeChart lineChart;
    private DefaultCategoryDataset dataset;
    public XYSeries tempInt;
    public XYSeries tempExt;
    private long count;
    public ValueMarker limit;
    public LineChart( String applicationTitle , String chartTitle ) {

        super(applicationTitle);
        lineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "Temps"," deg C",
                createDataset(),
                PlotOrientation.VERTICAL,
                true,true,false);

        lineChart.setBackgroundPaint(new Color(214,217,217)); // couleur background 77
        lineChart.getTitle().setPaint(new Color(255, 255, 255));

        setTitle(null);
        getJChart().setTitle((String)null);
        getJChart().getLegend().setPosition(RectangleEdge.RIGHT);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 400 ) );
        setContentPane( chartPanel );

        XYPlot plot = lineChart.getXYPlot();
        count = 0;
        /* limit ici est la consigne la temperature int ne devrait pas depasser ceci*/
        limit = new ValueMarker(1, Color.PINK, new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2.0f, new float[]{5.0f}, 0.0f)); //
        plot.addRangeMarker(limit, Layer.BACKGROUND);

        plot.setBackgroundPaint(new Color(45,45,45));  //45,45,45
        plot.setRangeGridlinePaint(new Color(10,10,10)); //Color of the grids in the axis
        plot.setDomainGridlinePaint(new Color(10,10,10)); // -||-

        plot.getDomainAxis().setLabelPaint(new Color(117, 107, 107)); //color of axis bar (X)
        plot.getRangeAxis().setLabelPaint(new Color(103, 97, 97)); // -||-  (Y)

        XYItemRenderer renderer = plot.getRenderer();

        // Temperature interne
        renderer.setSeriesItemLabelPaint(0, new Color(121, 243, 128));
        renderer.setSeriesPaint(0, new Color(121, 243, 128));
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));  //tirets

        //Temperature externe
        renderer.setSeriesItemLabelPaint(1, new Color(241, 33, 33));
        renderer.setSeriesPaint(1, new Color(241, 33, 33));
        renderer.setSeriesStroke(1, new BasicStroke(4.0f));

        plot.getRangeAxis().setRange(new Range(10, 35)); // Intervalle axe temperature
        //plot.getRangeAxis().setTickLabelPaint(new Color(200,200,200));

//		  plot.setBackgroundPaint(new Color(77,77,77));
        //Manip Legende
        lineChart.getLegend().setBackgroundPaint(new Color(255, 255, 255));
        lineChart.getLegend().setItemPaint(new Color(56, 63, 63));
        lineChart.getLegend().setBorder(0, 0, 0, 0); //*/
    }


    public XYSeriesCollection createDataset( ) {
        //DefaultCategoryDataset dataset = new DefaultCategoryDataset( ); // good but since seen new at codejava.net
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries tempInt = new XYSeries("Temperature interne"); //legende graphe en rouge (lorsqu'il n'ya rien definit il prend le rouge puis le bleu par defaut
        XYSeries tempExt = new XYSeries("Temperature externe "); //legende graphe en vert
        //XYSeries Consignechart = new XYSeries("Object 3");

        dataset.addSeries(tempInt);
        dataset.addSeries(tempExt);

        /*dataset.addValue( 15 , "schools" , "1970" );
        dataset.addValue( 30 , "schools" , "1980" );
        dataset.addValue( 60 , "schools" ,  "1990" );
        dataset.addValue( 120 , "schools" , "2000" );
        dataset.addValue( 240 , "schools" , "2010" );
        dataset.addValue( 300 , "schools" , "2014" ); //*/

        return dataset;
    }

    public JFreeChart getJChart() {
        return lineChart;
    }

    public void addData(float tempInt,float tempExt)
    {
        count++;
        this.tempInt.add(count,tempInt);
        this.tempExt.add(count,tempExt);
    }

    public static void main( String[ ] args ) {
        LineChart chart = new LineChart(
                " " ,
                "Number of Schools vs years");

        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }
}
