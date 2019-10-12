package subway;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Dijkstra {
	 private static HashMap<Station, Result> resultMap = new HashMap<>();
	    private static List<Station> analysisList = new ArrayList<>();//存放已经分析过的站点（已被分析表示起始点到该点的最短路径已求出）
	    public static Result calculate(Station star, Station end) {
	        if (!analysisList.contains(star)) {
	            analysisList.add(star);
	        }//将star站点放到以及分析的站点中去
	        if (star.equals(end)) {
	            Result result = new Result();
	            result.setDistance(0.0D);
	            result.setEndStation(star);
	            result.setStarStation(star);
	            return resultMap.put(star, result);
	        }//当star站点等于end站点，则设置result的距离为0，end站点为star站点。
	        if (resultMap.isEmpty()) {  //当第一次调用calculate，并且起始点和终止点不同，那么resultMap为空。
	            List<Station> linkStations = getLinkStations(star); //把相邻站点集合中的所有站点，加入resultMap中。
	            for (Station station : linkStations) {
	                Result result = new Result();
	                result.setStarStation(star);
	                result.setEndStation(station);
	                String key = star.getName() + ":" + station.getName();
	                Double distance = Builder.getDistance(key);
	                result.setDistance(distance);
	                result.getPassStation().add(station);
	                resultMap.put(station, result);
	            }
	        }
	        Station parent = getNextStation();
	        if (parent == null) {//如果resultMap所有点keySet被分析完了，则返回的parent为null。
	            Result result = new Result();
	            result.setDistance(0.0D);
	            result.setStarStation(star);
	            result.setEndStation(end);
	            //put方法的返回值就是value值。
	            return resultMap.put(end, result);
	        }
	        //如果得到的最佳邻点与目标点相同，则直接返回最佳邻点对应的result对象。
	        if (parent.equals(end)) {
	            return resultMap.get(parent);
	        }
	        //在路径经过点中加入parent后，更新resultMap集合，要么起始点经过parent达到parent相邻点是最优的，要么起始点到parent相邻点不可达，而通过parent可达。
	        //获取parent对象(最佳点)的相邻点。
	        //分析一个parent最佳点后，把它的相邻点都会加入到resultMap中，在下一次调用getNextStation获取resultMap中未被标记且距离（起始点到该station的距离）最短。
	        List<Station> childLinkStations = getLinkStations(parent);
	        for (Station child : childLinkStations) {
	            if (analysisList.contains(child)) {
	                continue;
	            }
	            String key = parent.getName() + ":" + child.getName();
	            Double distance;
	            distance = Builder.getDistance(key);
	            Builder.getDistance(key);
	            if (parent.getName().equals(child.getName())) {
	                distance = 0.0D;
	            }
	            Double parentDistance = resultMap.get(parent).getDistance();
	            distance = doubleAdd(distance,parentDistance);
	            List<Station> parentPassStations = resultMap.get(parent).getPassStation();
	            Result childResult = resultMap.get(child);
	            if (childResult != null) {
	                if (childResult.getDistance() > distance) { //如果通过最佳点比直接到距离小，则更新resultMap中的对应result对象。
	                    childResult.setDistance(distance);
	                    childResult.getPassStation().clear();
	                    childResult.getPassStation().addAll(parentPassStations);
	                    childResult.getPassStation().add(child);//路径更新为A->最佳点->child点。
	                }
	            } else {
	                //如果在resultMap中没有最佳点的相邻点，则往resultMap中添加通过最佳点（初始为起始点的最佳邻点）到达该点。
	                childResult = new Result();
	                childResult.setDistance(distance);
	                childResult.setStarStation(star);
	                childResult.setEndStation(child);
	                childResult.getPassStation().addAll(parentPassStations);
	                childResult.getPassStation().add(child);
	            }
	            resultMap.put(child, childResult);
	        }
	        analysisList.add(parent);
	         calculate(star, end); 
	         return resultMap.get(end);
	    }
	    public static void reMake() {
	    	analysisList.clear();;
	    	resultMap.clear();;
	    }
	    public static List<Station> getLinkStations(Station station) {
	        List<Station> linkedStaions = new ArrayList<Station>();

	       for (List<Station> line : Builder.lineSet) {//遍历每条地铁线
	            for (int i = 0; i < line.size(); i++) {
	                if (station.equals(line.get(i))) {
	                    if (i == 0) {   //如果该站点位于地铁线的起始站，则相邻站为地铁线的第二个站点(i+1)，
	                        linkedStaions.add(line.get(i + 1));
	                    } else if (i == (line.size() - 1)) {//如果该站点位于地铁线的最后一个站，则相邻站为地铁线的倒数第二个站点（i-1），
	                        linkedStaions.add(line.get(i - 1));
	                    } else {  //如果该站点位于地铁线的其余位置，则相邻站点为该站点前后位置（i-1/i+1）
	                        linkedStaions.add(line.get(i + 1));
	                        linkedStaions.add(line.get(i - 1));
	                    }
	                }
	            }
	        }
	        return linkedStaions;
	    }
	    private static Station getNextStation() {
	        Double min = Double.MAX_VALUE;
	        Station rets = null;
	        Set<Station> stations = resultMap.keySet();//获取resultMap中的station集合
	        for (Station station : stations) {
	            if (analysisList.contains(station)) {//如果该点被标记为“已被分析”,则跳过分析
	                continue;
	            }
	            //循环比较resultMap中未被标记的点，求出最短路径的result对象。
	            Result result = resultMap.get(station);
	            if (result.getDistance() < min) {
	                min = result.getDistance();
	                rets = result.getEndStation();
	            }
	        }
	        return rets;//返回下一个站点
	    }
	    private static double doubleAdd(double v1, double v2) {
	        BigDecimal b1 = new BigDecimal(Double.toString(v1));
	        BigDecimal b2 = new BigDecimal(Double.toString(v2));
	        return b1.add(b2).doubleValue();
	    }
}
