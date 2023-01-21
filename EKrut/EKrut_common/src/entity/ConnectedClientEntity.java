package entity;

public class ConnectedClientEntity
{
    private String ip;
    private String host;
    private String status;
    

    /**
     * Instantiates a new connected client entity.
     *
     * @param ip the ip
     * @param host the host
     * @param status the status
     */
    public ConnectedClientEntity(final String ip, final String host, final String status) {
        this.ip = ip;
        this.host = host;
        this.status = status;
    }
    
    /**
     * Sets the ip.
     *
     * @param ip the new ip
     */
    public void setIp(final String ip) {
        this.ip = ip;
    }
    
    /**
     * Gets the ip.
     *
     * @return the ip
     */
    public String getIp() {
        return this.ip;
    }
    
    /**
     * Sets the host.
     *
     * @param host the new host
     */
    public void setHost(final String host) {
        this.host = host;
    }
    
    /**
     * Gets the host.
     *
     * @return the host
     */
    public String getHost() {
        return this.host;
    }
    
    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(final String status) {
        this.status = status;
    }
    
    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return this.status;
    }
    
    @Override
    public String toString() {
        return "[ip = " + this.ip + ", host = " + this.host + ", status = " + this.status + "]";
    }
    
}