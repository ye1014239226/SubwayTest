package subway;
import java.io.File;
import java.util.Scanner;
public class test {
	 public static void main(String[] args) {
	        read();
	        System.out.println("��·��001,002,004,005,006,007,008,009,010,013,14��,14��,015,��ͨ��,��ƽ��,��ׯ��,��ɽ��,������");
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
	    	System.out.print("����ָ�(��ѯ������·��Ϣ��-a 001���ߣ�,����ѯ��ĩվ��·��-b ƻ��԰ ��Ȫ·��:");
	         String s=input.nextLine();
	         String[] split =s.split("\\s+");
	         switch(split[0]) {
	         case "-map":
	        	 if(split.length==1){
	                   Builder.readSubway();
	                   System.out.println("�ɹ���ȡsubway.txt�ļ�");
	               }else{
	                   System.out.println("��������");
	               }
	               break;
	         case "-a":
	              if(split.length==2){
	                   Builder.readSubway();
	                   Builder.getLineDate(split[1]);
	               }else{

	                   System.out.println("��������");
	               }
	               break;
	           case "-b":
	               if(split.length==3){
	                   Builder.readSubway();
	                   Result result = Dijkstra.calculate(new Station(split[1]), new Station(split[2]));
	                   Builder.getPassStation(result);
	               }else{
	                   System.out.println("��������");
	               }
	               break;
	         }
		    	System.out.println();
	    }
}
