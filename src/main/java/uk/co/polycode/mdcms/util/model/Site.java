package uk.co.polycode.mdcms.util.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Site attributes
 * 
 * @author Antony
 */
@Component("site")
public class Site implements Serializable {

	private static final long serialVersionUID = 1L;

   /**
    * The VAT rate to apply
    */
   private float vat;

   /**
    * The VAT number used
    */
   private String vatNumber;

   /**
    * The language tag http://en.wikipedia.org/wiki/IETF_language_tag
    */
   private String language;

   /**
    * The logo to identify the site
    */
   private String logo;

   /**
    * The cannonical URL for the site
    */
   private String url;

   public String getPaypalHostedButtonId() {
      return paypalHostedButtonId;
   }

   public void setPaypalHostedButtonId(String paypalHostedButtonId) {
      this.paypalHostedButtonId = paypalHostedButtonId;
   }

   public String getPaypalHostedButtonType() {
      return paypalHostedButtonType;
   }

   public void setPaypalHostedButtonType(String paypalHostedButtonType) {
      this.paypalHostedButtonType = paypalHostedButtonType;
   }

   public String getPaypalHostedButtonEnvironment() {
      return paypalHostedButtonEnvironment;
   }

   public void setPaypalHostedButtonEnvironment(String paypalHostedButtonEnvironment) {
      this.paypalHostedButtonEnvironment = paypalHostedButtonEnvironment;
   }

   /**
    * The id of the PayPal button to use, e.g. 37EV7JXJ5UY6A
    * From property site.paypal.hostedbutton.id
    */
   private String paypalHostedButtonId;

   /**
    * The type of PayPal button to use, either 'business' or 'hosted_button_id'
    * From property site.paypal.hostedbutton.type
    */
   private String paypalHostedButtonType;

   /**
    * The environment to use for the PayPal button either 'sandbox' or 'production'
    * From property site.paypal.hostedbutton.environment
    */
   private String paypalHostedButtonEnvironment;

   /**
    * The name of the site
    */
   private String name;

   /**
    * The Facebook page associated with the site
    */
   private String fb;

   /**
    * The organisation responsible for the site
    */
   private String organisation;

   /**
    * The primary email address for things related to the site
    */
   private String email;

   /**
    * The VAT rate to apply
    * 
    * @return the vat
    */
   public float getVat() {
      return this.vat;
   }

   /**
    * The VAT rate to apply
    * 
    * @param vat
    *           the vat to set
    */
   public void setVat(final float vat) {
      this.vat = vat;
   }

   /**
    * The language tag http://en.wikipedia.org/wiki/IETF_language_tag
    * 
    * @return the language
    */
   public String getLanguage() {
      return this.language;
   }

   /**
    * The language tag http://en.wikipedia.org/wiki/IETF_language_tag
    * 
    * @param language
    *           the language to set
    */
   public void setLanguage(final String language) {
      this.language = language;
   }

   /**
    * The logo to identify the site
    * 
    * @return the logo
    */
   public String getLogo() {
      return this.logo;
   }

   /**
    * The logo to identify the site
    * 
    * @param logo
    *           the logo to set
    */
   public void setLogo(final String logo) {
      this.logo = logo;
   }

   /**
    * The cannonical URL for the site
    * 
    * @return the url
    */
   public String getUrl() {
      return this.url;
   }

	/**
	 * The host from the cannonical URL for the site
	 *
	 * @return the host
	 */
	public String getHostFromUrl() {
		if (StringUtils.isNotBlank(this.url)){
			try {
				URI uri = new URI(this.url);
				return uri.getHost();
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException("Site.url should always parse as a URL: " + this.url, e);
			}
		}else{
			return null;
		}
	}

   /**
    * The cannonical URL for the site
    * 
    * @param url
    *           the url to set
    */
   public void setUrl(final String url) {
      this.url = url;
   }

   /**
    * The name of the site
    * 
    * @return the name
    */
   public String getName() {
      return this.name;
   }

   /**
    * The name of the site
    * 
    * @param name
    *           the name to set
    */
   public void setName(final String name) {
      this.name = name;
   }

   /**
    * The Facebook page associated with the site
    * 
    * @return the fb
    */
   public String getFb() {
      return this.fb;
   }

   /**
    * The Facebook page associated with the site
    * 
    * @param fb
    *           the fb to set
    */
   public void setFb(final String fb) {
      this.fb = fb;
   }

   /**
    * The organisation responsible for the site
    * 
    * @return the organisation
    */
   public String getOrganisation() {
      return this.organisation;
   }

   /**
    * The organisation responsible for the site
    * 
    * @param organisation
    *           the organisation to set
    */
   public void setOrganisation(final String organisation) {
      this.organisation = organisation;
   }

   /**
    * The VAT number used
    * 
    * @return the vatNumber
    */
   public String getVatNumber() {
      return this.vatNumber;
   }

   /**
    * The VAT number used
    * 
    * @param vatNumber
    *           the vatNumber to set
    */
   public void setVatNumber(final String vatNumber) {
      this.vatNumber = vatNumber;
   }

   /**
    * The primary email address for things related to the site
    * 
    * @return the email
    */
   public String getEmail() {
      return this.email;
   }

   /**
    * The primary email address for things related to the site
    * 
    * @param email
    *           the email to set
    */
   public void setEmail(final String email) {
      this.email = email;
   }

}
