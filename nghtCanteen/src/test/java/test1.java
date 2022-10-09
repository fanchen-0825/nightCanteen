import org.junit.jupiter.api.Test;

public class test1 {
    @Test
    public void upload(){
        String filename="123.jpg";
        System.out.println(filename.substring(filename.lastIndexOf(".")));
    }
}
