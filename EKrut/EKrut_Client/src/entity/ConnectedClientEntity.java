package entity;

public class ConnectedClientEntity
{
    private String ip;
    private String host;
    private String status;
    

    public ConnectedClientEntity(final String ip, final String host, final String status) {
        this.ip = ip;
        this.host = host;
        this.status = status;
    }
    
    public void setIp(final String ip) {
        this.ip = ip;
    }
    
    public String getIp() {
        return this.ip;
    }
    
    public void setHost(final String host) {
        this.host = host;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    @Override
    public String toString() {
        return "[ip = " + this.ip + ", host = " + this.host + ", status = " + this.status + "]";
    }
    
}