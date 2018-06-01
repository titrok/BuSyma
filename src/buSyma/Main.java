package buSyma;

public class Main {

	public Main() {
	}
	
	public void start () {
		String [] args = new String []{ System.getProperty("user.dir") + "/BuSyma.rs",};
		repast.simphony.runtime.RepastMain.main ( args ) ;
		}
	
	public static void main(String[] args) {
		Main m = new Main();
		m.start();
	}

}
