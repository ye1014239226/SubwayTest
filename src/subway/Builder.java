package subway;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Builder {
	public  static String FILE_PATH;
    public  static String WRITE_PATH;
    public static HashMap<String,HashMap<String,Double>> distanceMap = new HashMap<String,HashMap<String,Double>>();
    public static LinkedHashSet<List<Station>> lineSet = new LinkedHashSet<>();//所有线集合
    public static HashMap<String,List<Station>> lineData;
    private Builder() {
    }

    static {
    	Create();
    }
    public static void Create(){
        lineData = new HashMap<>();
        for (List<Station> stations : lineSet) {
            lineData.put(stations.get(1).getLine(),stations);
        }
    }
    public static String getLineNameByStation(Station station){
    	Create();
        String startname = station.getName();
        for (Map.Entry<String,List<Station>> entry : lineData.entrySet()) {
            List<Station> stations =  entry.getValue();
            for (Station sta : stations){
                if(sta.getName().equals(startname)){
                    return entry.getKey();
                }
            }
        }
        return "";
    }
    public static Double getDistance(String key) {//获取两个站之间的距离
        return distanceMap.entrySet().stream().filter(x->x.getValue().keySet().contains(key)).findFirst().get().getValue().get(key);
    }
    public static String getLineName(String key) {//获取地铁线路名字
        return distanceMap.keySet().stream().filter(x -> distanceMap.get(x).containsKey(key)).findFirst().orElse("");
    }
    public static boolean isContains(String key){
        return distanceMap.entrySet().stream().anyMatch(x->x.getValue().keySet().contains(key));
    }
    public static ArrayList<Station> getLine(String lineStr,String lineName){
        ArrayList<Station> line =  new ArrayList<Station>();
        String[] lineArr = lineStr.split(",");
        for (String s : lineArr) {
            line.add(new Station(s,lineName));
        }
        return line;
    }

    public static void getLineDate(String lineName) {
    	Create();
    	lineName=lineName.substring(0,3);
    	 List<Station> lineInfo = lineData.get(lineName);
    	 String lineStr = lineInfo.stream().map(x->x.getName()).collect(Collectors.joining(",","[","]"));
    	 System.out.print(lineStr);
    }
    public static void getPassStation(Result result){
        Station starStation = result.getStarStation();
        String starLine = getLineNameByStation(starStation);
        String converLine = starLine;
        System.out.println("起始地铁线："+starLine);
        for (Station station : result.getPassStation()) {
            if(!converLine.equals(station.getLine())){
                System.out.print("(换乘地铁线："+station.getLine()+")");
                converLine = station.getLine();
                converLine = station.getLine();
            }
            System.out.print(station.getName() + "->");
        }
    }
    public static void readSubway() {
        File file = new File(FILE_PATH);
        BufferedReader reader = null;

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file),"UTF-8");

            reader = new BufferedReader(inputStreamReader);
            String line = null;
            String lineName = "001";
            distanceMap.put("001",new HashMap<>());
            while ((line = reader.readLine()) != null) {
                if(line.trim().length()==1||line.trim().length()==3||line.trim().length()==2){
                    if(line.trim().length()==3||line.trim().length()==2){ //  \uFEFF 默认以这个开头！！！
                        continue;
                    }
                    lineName = line;
                    if(!distanceMap.keySet().contains(line)){
                        distanceMap.put(line.trim(),new HashMap<>());
                    }
                }else{
                    if(line.trim().startsWith("*")){
                        String[] lineInfo = line.substring(1).split("-");
                        lineSet.add(getLine(lineInfo[1].trim(),lineInfo[0].trim()));
                    }else{
                        String texts[] = line.split("\t");
                        String key = texts[0].trim();
                        Double value = Double.valueOf(texts[1]);
                        distanceMap.get(lineName).put(key,value);
                        String other = key.split(":")[1].trim()+":"+key.split(":")[0].trim();
                        distanceMap.get(lineName).put(other,value);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
