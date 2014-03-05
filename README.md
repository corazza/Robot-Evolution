rtevo
=====

Robot Evolution is an application that uses genetic algorithms to evolve and optimize virtual walking poly-pedal robots. The robots are 2D geometric constructions of rectangles that are connected by virtual motors which apply torque to these rectangles, making them move.

Example:

![Example robot](http://i1.wp.com/yannbane.com/wordpress/wp-content/uploads/2014/03/walker1.png?resize=210%2C199)

We use Java and technologies such as JBox2D, Java Concurrent and Swing in order to create an application that will enable users to witness the ongoing evolution process, while much of the work is being done in the backgruond.

The program that we have developed is an interesting example of software inspiried by biological phenomena, and has many useful applications.

The user interface looks like this:

![User interface](http://i0.wp.com/yannbane.com/wordpress/wp-content/uploads/2014/03/ui.png?resize=604%2C313)

It also runs on servers:

![rtevo.jar threads on a Linux server](http://i1.wp.com/yannbane.com/wordpress/wp-content/uploads/2014/03/onServer.png?resize=604%2C394)

#Usage

You just need to compile the program into a JAR with all the dependencies and run it (it will use the [default configuration](https://github.com/yannbane/rtevo/blob/master/rtevo/src/main/resources/default-configuration.properties)):

`java -jar rtevo.jar`

An alternative configuration file can be specified on the command line:

`java -jar rtevo.jar configuration.properties`

...where configuration.properties could be the edited default configuration file supplied earlier (which is also available when the JAR file is extracted).

##Croatian

Robot Evolution je program koji koristi genetske algoritme kako bi optimizirao dizajn hodajućih robota. Koristimo Javu i tehnologije poput JBox2Da, Java Concurrent i Swinga kako bi napravili aplikaciju koja će korisnicima omogućiti da toj evoluciji svjedoče uživo, dok se većina posla obavlja u pozadini.

Program koji smo razvili zanimljiv je primjer softvera koji je inspiriran prirodnim fenomenima te ima mnogo korisnih primjena. 
