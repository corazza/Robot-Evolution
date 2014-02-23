rtevo
=====

Robot Evolution is a program that uses genetic algorithms in order to optimize the design of walking robots.

Example:

insert picture after post

We use Java and technologies such as JBox2D, Java Concurrent and Swing in order to create an application that will enable users to witness the ongoing evolution process, while much of the work is being done in the backgruond.

The program that we have developed is an interesting example of software inspiried by biological phenomena, and has many useful applications.

The user interface looks like this:

insert picture after post

#Usage

You just need to compile the program into a JAR with all the dependencies and run it (it will use the [default configuration](https://github.com/yannbane/rtevo/blob/master/rtevo/src/main/resources/default-configuration.properties)):

`java -jar rtevo.jar`

An alternative configuration file can be specified on the command line:

`java -jar rtevo.jar configuration.properties`

...where configuration.properties could be the edited default configuration file supplied earlier (which is also available when the JAR file is extracted).

##Croatian

Robot Evolution je program koji koristi genetske algoritme kako bi optimizirao dizajn hodajućih robota. Koristimo Javu i tehnologije poput JBox2Da, Java Concurrent i Swinga kako bi napravili aplikaciju koja će korisnicima omogućiti da toj evoluciji svjedoče uživo, dok se većina posla obavlja u pozadini.

Program koji smo razvili zanimljiv je primjer softvera koji je inspiriran prirodnim fenomenima te ima mnogo korisnih primjena. 
