package com.dawei.core.pojo.product;

import java.io.Serializable;
import java.util.Date;

public class Sku implements Serializable {
    /**
     * ID
     */
    private Long id;

    /**
     * ��ƷID
     */
    private Long productId;

    /**
     * ��ɫID
     */
    private Long colorId;

    /**
     * ����
     */
    private String size;

    /**
     * �г���
     */
    private Float marketPrice;

    /**
     * �ۼ�
     */
    private Float price;

    /**
     * �˷� Ĭ��10Ԫ
     */
    private Float deliveFee;

    /**
     * ���
     */
    private Integer stock;

    /**
     * ��������
     */
    private Integer upperLimit;

    private Date createTime;
    
    private Color color;
    
    private Product product;
    
    
    
    
    public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public Float getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Float marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getDeliveFee() {
        return deliveFee;
    }

    public void setDeliveFee(Float deliveFee) {
        this.deliveFee = deliveFee;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Integer upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", colorId=").append(colorId);
        sb.append(", size=").append(size);
        sb.append(", marketPrice=").append(marketPrice);
        sb.append(", price=").append(price);
        sb.append(", deliveFee=").append(deliveFee);
        sb.append(", stock=").append(stock);
        sb.append(", upperLimit=").append(upperLimit);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
    
    
    @Override
    public int hashCode() {
    	// TODO Auto-generated method stub
    	
    	return super.hashCode();
    }
    @Override
    public boolean equals(Object arg0) {
    	// TODO Auto-generated method stub
    	return super.equals(arg0);
    }
}