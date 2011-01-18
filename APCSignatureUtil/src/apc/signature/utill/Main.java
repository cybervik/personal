/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apc.signature.utill;

import com.websina.license.License;
import com.websina.license.LicenseManager;
import com.websina.license.SignatureUtil;
import com.websina.util.ByteHex;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vikram S
 */
public class Main {

    private static String privateKey = "3082014C0201003082012C06072A8648CE3804013082011F02818100FD7F53811D75122952DF4A9C2EECE4E7F611B7523CEF4400C31E3F80B6512669455D402251FB593D8D58FABFC5F5BA30F6CB9B556CD7813B801D346FF26660B76B9950A5A49F9FE8047B1022C24FBBA9D7FEB7C61BF83B57E7C6A8A6150F04FB83F6D3C51EC3023554135A169132F675F3AE2B61D72AEFF22203199DD14801C70215009760508F15230BCCB292B982A2EB840BF0581CF502818100F7E1A085D69B3DDECBBCAB5C36B857B97994AFBBFA3AEA82F9574C0B3D0782675159578EBAD4594FE67107108180B449167123E84C281613B7CF09328CC8A6E13C167A8B547C8D28E0A3AE1E2BB3A675916EA37F0BFA213562F1FB627A01243BCCA4F1BEA8519089A883DFE15AE59F06928B665E807B552564014C3BFECF492A041702150083996E6EC7CE39B748DDF28BE6F03211F1E6258C";
    private static String publicKey = "308201B73082012C06072A8648CE3804013082011F02818100FD7F53811D75122952DF4A9C2EECE4E7F611B7523CEF4400C31E3F80B6512669455D402251FB593D8D58FABFC5F5BA30F6CB9B556CD7813B801D346FF26660B76B9950A5A49F9FE8047B1022C24FBBA9D7FEB7C61BF83B57E7C6A8A6150F04FB83F6D3C51EC3023554135A169132F675F3AE2B61D72AEFF22203199DD14801C70215009760508F15230BCCB292B982A2EB840BF0581CF502818100F7E1A085D69B3DDECBBCAB5C36B857B97994AFBBFA3AEA82F9574C0B3D0782675159578EBAD4594FE67107108180B449167123E84C281613B7CF09328CC8A6E13C167A8B547C8D28E0A3AE1E2BB3A675916EA37F0BFA213562F1FB627A01243BCCA4F1BEA8519089A883DFE15AE59F06928B665E807B552564014C3BFECF492A0381840002818020E5011F3FB1495B3FF451215923E41E7FAD08D706A613BE70A24CED191CE2D09F0AAA18DA53D6A5E2FFC8EA1355D1ACC630E3B932CA137E27C1ACF6B85052288202CA14EC1DD13DFDDB4F3BBE4B642692DC604166EF5D76A310BA1018602E34C4EC3C1D417502482047C437C6F16E2DA52F0B4FFE0B9452B4B976D80F5209E6";
    private static LicenseManager manager;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws GeneralSecurityException {
        testManager();
        createNewLicense();
    }

    private static void testManager() throws GeneralSecurityException {
        manager = LicenseManager.getInstance();
        System.out.println("License is valid? - " + manager.isValid());
        System.out.println("Days left - " + manager.daysLeft());
        printFeature("Licensor");
        printFeature("Licensee");
        printFeature("Signature");
    }

    private static void printFeature(final String feature) {
        System.out.println(feature + " - " + manager.getFeature(feature));
    }

    private static void createNewLicense() throws GeneralSecurityException {
        License l2 = License.newLicense();
        l2.setFeature("Licensor", "Dimatas Technologies LLC");
        l2.setFeature("Licensee", "Vikram");
        l2.setFeature("License Type", "1");
        l2.setFeature("License Sub Type", "2");
        l2.setFeature("Email", "user1@vikram.com");
        l2.setExpiration("2012-1-18");
        System.out.println(l2.format());
        final String signature = SignatureUtil.sign(l2.format(), ByteHex.convert(privateKey));
        System.out.println("Signature - " + signature);
        final boolean verificationStatus = SignatureUtil.verify(l2.format(), ByteHex.convert(signature), ByteHex.convert(publicKey));
        System.out.println("Signature Verified? - " + verificationStatus);
    }
}
