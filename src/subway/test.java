package subway;
import java.io.File;
import java.util.Scanner;
public class test {
	 public static void main(String[] args) {
	        read();
	        System.out.println("线路：001,002,004,005,006,007,008,009,010,013,14东,14西,015,八通线,昌平线,亦庄线,房山线,机场线");
	      while(true) {
	        write();
	        Dijkstra.reMake();
	        }
	    }
	    public static void read(){
	    	Builder.FILE_PATH="d:\\subway2.txt";
	        Builder.readSubway();
	    }
	    public static void write() {
	    	Scanner input=new Scanner(System.in);
	    	System.out.print("输入指令：(查询地铁线路信息：-a 001号线）,（查询起末站线路：-b 苹果园 玉泉路）:");
	         String s=input.nextLine();
	         String[] split =s.split("\\s+");
	         switch(split[0]) {
	         case "-map":
	        	 if(split.length==1){
	                   Builder.readSubway();
	                   System.out.println("成功读取subway.txt文件");
	               }else{
	                   System.out.println("重新输入");
	               }
	               break;
	         case "-a":
	              if(split.length==2){
	                   Builder.readSubway();
	                   Builder.getLineDate(split[1]);
	               }else{

	                   System.out.println("重新输入");
	               }
	               break;
	           case "-b":
	               if(split.length==3){
	                   Builder.readSubway();
	                   Result result = Dijkstra.calculate(new Station(split[1]), new Station(split[2]));
	                   Builder.getPassStation(result);
	               }else{
	                   System.out.println("重新输入");
	               }
	               break;
	         }
		    	System.out.println();
	    }
}
