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
	    private static List<Station> analysisList = new ArrayList<>();//����Ѿ���������վ�㣨�ѱ�������ʾ��ʼ�㵽�õ�����·���������
	    public static Result calculate(Station star, Station end) {
	        if (!analysisList.contains(star)) {
	            analysisList.add(star);
	        }//��starվ��ŵ��Լ�������վ����ȥ
	        if (star.equals(end)) {
	            Result result = new Result();
	            result.setDistance(0.0D);
	            result.setEndStation(star);
	            result.setStarStation(star);
	            return resultMap.put(star, result);
	        }//��starվ�����endվ�㣬������result�ľ���Ϊ0��endվ��Ϊstarվ�㡣
	        if (resultMap.isEmpty()) {  //����һ�ε���calculate��������ʼ�����ֹ�㲻ͬ����ôresultMapΪ�ա�
	            List<Station> linkStations = getLinkStations(star); //������վ�㼯���е�����վ�㣬����resultMap�С�
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
	        if (parent == null) {//���resultMap���е�keySet���������ˣ��򷵻ص�parentΪnull��
	            Result result = new Result();
	            result.setDistance(0.0D);
	            result.setStarStation(star);
	            result.setEndStation(end);
	            //put�����ķ���ֵ����valueֵ��
	            return resultMap.put(end, result);
	        }
	        //����õ�������ڵ���Ŀ�����ͬ����ֱ�ӷ�������ڵ��Ӧ��result����
	        if (parent.equals(end)) {
	            return resultMap.get(parent);
	        }
	        //��·���������м���parent�󣬸���resultMap���ϣ�Ҫô��ʼ�㾭��parent�ﵽparent���ڵ������ŵģ�Ҫô��ʼ�㵽parent���ڵ㲻�ɴ��ͨ��parent�ɴ
	        //��ȡparent����(��ѵ�)�����ڵ㡣
	        //����һ��parent��ѵ�󣬰��������ڵ㶼����뵽resultMap�У�����һ�ε���getNextStation��ȡresultMap��δ������Ҿ��루��ʼ�㵽��station�ľ��룩��̡�
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
	                if (childResult.getDistance() > distance) { //���ͨ����ѵ��ֱ�ӵ�����С�������resultMap�еĶ�Ӧresult����
	                    childResult.setDistance(distance);
	                    childResult.getPassStation().clear();
	                    childResult.getPassStation().addAll(parentPassStations);
	                    childResult.getPassStation().add(child);//·������ΪA->��ѵ�->child�㡣
	                }
	            } else {
	                //�����resultMap��û����ѵ�����ڵ㣬����resultMap�����ͨ����ѵ㣨��ʼΪ��ʼ�������ڵ㣩����õ㡣
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

	       for (List<Station> line : Builder.lineSet) {//����ÿ��������
	            for (int i = 0; i < line.size(); i++) {
	                if (station.equals(line.get(i))) {
	                    if (i == 0) {   //�����վ��λ�ڵ����ߵ���ʼվ��������վΪ�����ߵĵڶ���վ��(i+1)��
	                        linkedStaions.add(line.get(i + 1));
	                    } else if (i == (line.size() - 1)) {//�����վ��λ�ڵ����ߵ����һ��վ��������վΪ�����ߵĵ����ڶ���վ�㣨i-1����
	                        linkedStaions.add(line.get(i - 1));
	                    } else {  //�����վ��λ�ڵ����ߵ�����λ�ã�������վ��Ϊ��վ��ǰ��λ�ã�i-1/i+1��
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
	        Set<Station> stations = resultMap.keySet();//��ȡresultMap�е�station����
	        for (Station station : stations) {
	            if (analysisList.contains(station)) {//����õ㱻���Ϊ���ѱ�������,����������
	                continue;
	            }
	            //ѭ���Ƚ�resultMap��δ����ǵĵ㣬������·����result����
	            Result result = resultMap.get(station);
	            if (result.getDistance() < min) {
	                min = result.getDistance();
	                rets = result.getEndStation();
	            }
	        }
	        return rets;//������һ��վ��
	    }
	    private static double doubleAdd(double v1, double v2) {
	        BigDecimal b1 = new BigDecimal(Double.toString(v1));
	        BigDecimal b2 = new BigDecimal(Double.toString(v2));
	        return b1.add(b2).doubleValue();
	    }
}
