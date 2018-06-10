
package age.AgeClub.administrator.age_103;



import java.io.Serializable;

public class Discount implements Serializable{

    private String campaignName;
    private String campaignCategory;
    private String logoUrl;
    private String imageLink;
    private String campaignType;
    private int campaignRate;
    private String x;
    private String y;
    private int isShowcase;


    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignCategory() {
        return campaignCategory;
    }

    public void setCampaignCategory(String campaignCategory) {
        this.campaignCategory = campaignCategory;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public int getCampaignRate() {
        return campaignRate;
    }

    public void setCampaignRate(int campaignRate) {
        this.campaignRate = campaignRate;
    }

    public Double getX() {
        return Double.parseDouble(x);
    }

    public void setX(String x) {
        this.x = x;
    }

    public Double getY() {
        return Double.parseDouble(y);
    }

    public void setY(String y) {
        this.y = y;
    }

    public int getIsShowcase() {
        return isShowcase;
    }

    public void setIsShowcase(int isShowcase) {
        this.isShowcase = isShowcase;
    }
}
