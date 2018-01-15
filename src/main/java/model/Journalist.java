package model;

import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRNodeWrapperImpl;

import java.util.Map;
import java.util.Set;

public class Journalist {

    private String Title;
    private String AcademicTitle;
    private String Name;
    private String Surname;
    private String Adress;
    private String NPA;
    private String Place;
    private String PhoneNum;
    private String CellphoneNumber;
    private String Email;
    private Set<String> AdditionalEmail;
    private String NewspapersConcerned;
    private Set<String> Languages;
    private String Password;

    private Map<String, String> properties;

    private final JCRNodeWrapper journalistNode;
    private JCRNodeWrapper userNode;

    public Journalist(JCRNodeWrapper journalistNode) {
        this.journalistNode = journalistNode;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
        properties.put("Title", title);
    }

    public String getAcademicTitle() {
        return AcademicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.AcademicTitle = academicTitle;
        properties.put("AcademicTitle", academicTitle);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
        properties.put("Name", name);
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        this.Surname = surname;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        this.Adress = adress;
    }

    public String getNPA() {
        return NPA;
    }

    public void setNPA(String NPA) {
        this.NPA = NPA;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        this.Place = place;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.PhoneNum = phoneNum;
    }

    public String getCellphoneNumber() {
        return CellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.CellphoneNumber = cellphoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public Set<String> getAdditionalEmail() {
        return AdditionalEmail;
    }

    public void setAdditionalEmail(Set<String> additionalEmail) {
        this.AdditionalEmail = additionalEmail;
    }

    public String getNewspapersConcerned() {
        return NewspapersConcerned;
    }

    public void setNewspapersConcerned(String newspapersConcerned) {
        this.NewspapersConcerned = newspapersConcerned;
    }

    public Set<String> getLanguages() {
        return Languages;
    }

    public void setLanguages(Set<String> languages) {
        this.Languages = languages;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public JCRNodeWrapper getJournalistNode() {
        return journalistNode;
    }

    public JCRNodeWrapper getUserNode() {
        return userNode;
    }

    public void setUserNode(JCRNodeWrapper userNode) {
        this.userNode = userNode;
    }
}
