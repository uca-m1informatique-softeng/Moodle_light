# Dockers command for the project 

# Build the image 

 The dockersfiles for the images are in docker/* 
 You need to build the images from the root directory of the project Moodle_light 

You build the image with the command : 

` docker build -t 'name for the image' -f ' path to docker file' . ` 

/!\ don't forget the '.' at the end of the command which has to be the pwd of the main project workspace 

# 