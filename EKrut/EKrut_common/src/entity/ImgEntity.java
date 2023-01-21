package entity;

import java.io.Serializable;

public class ImgEntity implements Serializable {
	private String Description=null;
	private String imgName=null;	
	private int size=0;
	public  byte[] mybytearray;
	
	
	/**
	 * Inits the array.
	 *
	 * @param size the size
	 */
	public void initArray(int size)
	{
		mybytearray = new byte [size];	
	}
	
	/**
	 * Instantiates a new img entity.
	 *
	 * @param imgName the img name
	 */
	public ImgEntity( String imgName) {
		this.imgName = imgName;
	}
	
	
	/**
	 * Gets the img name.
	 *
	 * @return the img name
	 */
	public String getImgName() {
		return imgName;
	}

	/**
	 * Sets the img name.
	 *
	 * @param fileName the new img name
	 */
	public void setImgName(String fileName) {
		this.imgName = fileName;
	}
	
	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Gets the mybytearray.
	 *
	 * @return the mybytearray
	 */
	public byte[] getMybytearray() {
		return mybytearray;
	}
	
	/**
	 * Gets the mybytearray.
	 *
	 * @param i the i
	 * @return the mybytearray
	 */
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}

	/**
	 * Sets the mybytearray.
	 *
	 * @param mybytearray the new mybytearray
	 */
	public void setMybytearray(byte[] mybytearray) {
		
		for(int i=0;i<mybytearray.length;i++)
		this.mybytearray[i] = mybytearray[i];
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		Description = description;
	}	
}
