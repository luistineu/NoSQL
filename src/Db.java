import redis.clients.jedis.Jedis;

public class Db {

    	public Jedis connect() {
    		return new Jedis("localhost");
    	}
}
