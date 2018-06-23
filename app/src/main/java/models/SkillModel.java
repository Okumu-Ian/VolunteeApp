package models;

import java.io.Serializable;

/**
 * Created by The Architect on 6/16/2018.
 */

public class SkillModel implements Serializable{
    private String skill_id;
    private String skill_name;
    private String skill_image_url;
    private int skill_image_local;

    public int getSkill_image_local() {
        return skill_image_local;
    }

    public void setSkill_image_local(int skill_image_local) {
        this.skill_image_local = skill_image_local;
    }

    public SkillModel() {
    }

    public String getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(String skill_id) {
        this.skill_id = skill_id;
    }

    public String getSkill_name() {
        return skill_name;
    }

    public void setSkill_name(String skill_name) {
        this.skill_name = skill_name;
    }

    public String getSkill_image_url() {
        return skill_image_url;
    }

    public void setSkill_image_url(String skill_image_url) {
        this.skill_image_url = skill_image_url;
    }
}
