package com.ajibigad.corperwee.service;

import com.ajibigad.corperwee.exceptions.CorperWeeException;
import com.ajibigad.corperwee.exceptions.ResourceNotFoundException;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

/**
 * Created by Julius on 28/03/2016.
 */
@Service
public class ProfilePictureService {
    /*
    * This guy should help save the image to the file system and retrieve it also*/

    @Autowired
    UserRepository userRepository;

    public static final String DIRECTORY = "images";

    private static final Logger LOG = Logger.getLogger(ProfilePictureService.class);

    public String handleImageUpload(String imageBase64URI, String type, String username){
        // create a buffered image
        BufferedImage image = null;
        ByteArrayInputStream bis = null;
        byte[] imageByte;
        String imageName;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            imageByte = decoder.decodeBuffer(imageBase64URI);
            bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            imageName = save(image, type, username);
            LOG.info("Image Upload successful");
        } catch (IOException e) {
            LOG.error("Image Upload failed", e);
            e.printStackTrace();
            throw new CorperWeeException(e, "Image Upload Failed. Pls try again");
        }
        finally {
            if(bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    LOG.error("ByteArrayInputStream failed to close", e);
                    e.printStackTrace();
                }
            }
        }
        return imageName;
    }

    public String save(BufferedImage image, String type, String username) throws IOException {
        String savePath = createProfilePictureDirectory(username);
        String imageName = username + Calendar.getInstance().getTimeInMillis() + "." + type;
        ImageIO.write(image, type, new File(new File(savePath), username + Calendar.getInstance().getTimeInMillis() + "." + type));
        LOG.info("Image saving successful");
        User user = userRepository.findByUsername(username);
        user.setProfilePicture(imageName);
        userRepository.save(user);
        return imageName;
    }

    private String createProfilePictureDirectory(String username){
        StringBuilder sb = new StringBuilder();
        sb.append(DIRECTORY).append(File.separator).append("profilePicture").append(File.separator).append(username); // eg images/profilePicture/ajibigad
        String path = sb.toString();
        new File(path).mkdirs();
        LOG.info("Image folder creation successful");
        return path;
    }

//    private String getLatestProfilePictureName(String username){
//        File userProfilePictureDirs = new File(getUsernameProfilePictureDirectory(username));
//        if(userProfilePictureDirs.exists()){
//            //return the default name for the user
//            return "default.jpeg";
//        }
//        String [] pictures = userProfilePictureDirs.list();
//        Arrays.sort(pictures,
//                (s1, s2) -> {
//                    int s1ExtOffset= s1.lastIndexOf('.');
//                    int s2ExtOffset= s2.lastIndexOf('.');
//                    return (int) (Long.parseLong(s1.substring(username.length(), s1ExtOffset)) - Long.parseLong(s2.substring(username.length(), s2ExtOffset)));
//                }
//        );
//        return pictures[pictures.length-1];
//    }

    private String getUserProfilePictureName(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){ //this should be handled by a userService.. I now see why a service layer is needed so i would start with the user api first
            //i deal thing is userService.findByUsername.. the service would handle the exceptions
            throw new ResourceNotFoundException("Username " + username + " not found!");
        }
        return user.getProfilePicture();
    }

    public byte[] getImage(String username) throws IOException {
        String imageName = getUserProfilePictureName(username);
        if(imageName.equals("default.jpeg")){
            return  Files.readAllBytes(Paths.get(getDefaultProfilePictureDirectory() + File.separator + imageName));
        }
        return Files.readAllBytes(Paths.get(getUsernameProfilePictureDirectory(username) + File.separator + imageName));
    }

//    public String getImageName(String username){
//        return getUserProfilePictureName(username);
//    }

    private String getDefaultProfilePictureDirectory(){
        StringBuilder sb = new StringBuilder();
        sb.append(DIRECTORY).append(File.separator).append("profilePicture").append(File.separator).append("default"); // eg images/profilePicture/ajibigad
        return sb.toString();
    }

    private String getUsernameProfilePictureDirectory(String username){
        StringBuilder sb = new StringBuilder();
        sb.append(DIRECTORY).append(File.separator).append("profilePicture").append(File.separator).append(username); // eg images/profilePicture/ajibigad
        return sb.toString();
    }
 }
