package com.example.rotfl.streamingvideo;

/**
 * Created by rotfl on 13.04.2016.
 */
public class DataClass {
    String idCamera;
    String nameCamera;
    String rtspAdress;
    String login;
    String password;
    String port;
    String brightness;
    String contrast;
    String saturation;
    String hue;
    String portHTTP;

    public DataClass(String idCamera, String nameCamera, String rtspAdress, String login,  String password, String port, String portHTTP, String brightness, String contrast,String saturation, String hue  ) {

        this.brightness = brightness;
        this.contrast = contrast;
        this.hue = hue;
        this.idCamera = idCamera;
        this.login = login;
        this.nameCamera = nameCamera;
        this.password = password;
        this.rtspAdress = rtspAdress;
        this.saturation = saturation;
        this.port = port;
        this.portHTTP = portHTTP;
    }

    public DataClass() {
    }

    public String getPortHTTP() {
        return portHTTP;
    }

    public void setPortHTTP(String portHTTP) {
        this.portHTTP = portHTTP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    public String getContrast() {
        return contrast;
    }

    public void setContrast(String contrast) {
        this.contrast = contrast;
    }

    public String getHue() {
        return hue;
    }

    public void setHue(String hue) {
        this.hue = hue;
    }

    public String getIdCamera() {
        return idCamera;
    }

    public void setIdCamera(String idCamera) {
        this.idCamera = idCamera;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNameCamera() {
        return nameCamera;
    }

    public void setNameCamera(String nameCamera) {
        this.nameCamera = nameCamera;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRtspAdress() {
        return rtspAdress;
    }

    public void setRtspAdress(String rtspAdress) {
        this.rtspAdress = rtspAdress;
    }

    public String getSaturation() {
        return saturation;
    }

    public void setSaturation(String saturation) {
        this.saturation = saturation;
    }


}
