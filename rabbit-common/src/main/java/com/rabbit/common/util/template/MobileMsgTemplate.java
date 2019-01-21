package com.rabbit.common.util.template;

/**
 * @ClassName MobileMsgTemplate
 * @Description 短信通知模板
 * @Author LZQ
 * @Date 2019/1/19 19:17
 **/
public class MobileMsgTemplate {

    private String mobile;
    private String text;
    private String type;

    public MobileMsgTemplate(String mobile, String text, String type) {
        this.mobile = mobile;
        this.text = text;
        this.type = type;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getText() {
        return this.text;
    }

    public String getType() {
        return this.type;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MobileMsgTemplate)) return false;
        MobileMsgTemplate other = (MobileMsgTemplate) o;
        if (!other.canEqual(this)) return false;
        Object this$mobile = getMobile();
        Object other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) return false;
        Object this$text = getText();
        Object other$text = other.getText();
        if (this$text == null ? other$text != null : !this$text.equals(other$text)) return false;
        Object this$type = getType();
        Object other$type = other.getType();
        return this$type == null ? other$type == null : this$type.equals(other$type);
    }

    protected boolean canEqual(Object other) {
        return other instanceof MobileMsgTemplate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $mobile = getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        Object $text = getText();
        result = result * 59 + ($text == null ? 43 : $text.hashCode());
        Object $type = getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        return result;
    }

    public String toString() {
        return "MobileMsgTemplate(mobile=" + getMobile() + ", text=" + getText() + ", type=" + getType() + ")";
    }
}
