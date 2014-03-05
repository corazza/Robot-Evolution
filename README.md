Robot Evolution
===============

Robot Evolution (rtevo) is an application that uses genetic algorithms to evolve and optimize virtual walking poly-pedal robots. The robots are 2D geometric constructions of rectangles that are connected by virtual motors which apply torque to these rectangles, making them move.

Example:

![Example robot](http://i1.wp.com/yannbane.com/wordpress/wp-content/uploads/2014/03/walker1.png?resize=210%2C199)

We use Java and technologies such as JBox2D, Java Concurrent and Swing in order to create an application that will enable users to witness the ongoing evolution process, while much of the work is being done in the backgruond.

The program that we have developed is an interesting example of software inspiried by biological phenomena, and has many useful applications.

The user interface looks like this:

![User interface](http://i0.wp.com/yannbane.com/wordpress/wp-content/uploads/2014/03/ui.png?resize=604%2C313)

It also runs on servers:

![rtevo.jar threads on a Linux server](http://i1.wp.com/yannbane.com/wordpress/wp-content/uploads/2014/03/onServer.png?resize=604%2C394)

#Usage

You can download the program [here](https://drive.google.com/file/d/0B3IY1AV4ocikeE55UmVUclBhNkE/edit?usp=sharing). It is a 7-zipped file containing the JAR (with all the dependencies baked in) and a configuration file.

Run the program like this:

`java -jar rtevo.jar configuration.properties`

You can edit the configuration file to suit your needs.

##Croatian

Robot Evolution je program koji koristi genetske algoritme kako bi optimizirao dizajn hodajućih robota. Koristimo Javu i tehnologije poput JBox2Da, Java Concurrent i Swinga kako bi napravili aplikaciju koja će korisnicima omogućiti da toj evoluciji svjedoče uživo, dok se većina posla obavlja u pozadini.

Program koji smo razvili zanimljiv je primjer softvera koji je inspiriran prirodnim fenomenima te ima mnogo korisnih primjena. 
