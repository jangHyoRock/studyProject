package dhi.common.model.json;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Trend information value entity.
 */
public class Tag {

	private String unit;

    private ArrayList<HashMap<String, Object>> trend;

    private String name;

    public String getUnit ()
    {
        return unit;
    }

    public void setUnit (String unit)
    {
        this.unit = unit;
    }

    public ArrayList<HashMap<String, Object>> getTrend ()
    {
        return trend;
    }

    public void setTrend ( ArrayList<HashMap<String, Object>> trend)
    {
        this.trend = trend;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [unit = "+unit+", trend = "+trend+", name = "+name+"]";
    }    
}
