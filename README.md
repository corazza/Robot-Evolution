Robot Evolution
===============

Robot Evolution is an open source application that uses genetic algorithms to evolve and optimize virtual walking poly-pedal robots. The robots are 2D geometric constructions of rectangles that are connected by virtual motors which apply torque to these rectangles, making them move.

Example:

![Example robot](http://res.cloudinary.com/dhngozzmz/image/upload/v1443395262/walker1_mbyxar.png)

I wrote a blog post explaining most of the application: [jancorazza.com/2014/03/robot-evolution](http://jancorazza.com/2014/03/05/robot-evolution/).

We use Java and technologies such as JBox2D, Java Concurrent and Swing in order to create an application that will enable users to witness the ongoing evolution process, while much of the work is being done in the backgruond.

The program that we have developed is an interesting example of software inspiried by biological phenomena, and has many useful applications.

## Usage

Run the program like this:

    java -jar rtevo.jar configuration.properties

You can edit the configuration file to suit your needs.

## Croatian

Robot Evolution je program koji koristi genetske algoritme kako bi optimizirao dizajn hodajućih robota. Koristimo Javu i tehnologije poput JBox2Da, Java Concurrent i Swinga kako bi napravili aplikaciju koja će korisnicima omogućiti da toj evoluciji svjedoče uživo, dok se većina posla obavlja u pozadini.

Program koji smo razvili zanimljiv je primjer softvera koji je inspiriran prirodnim fenomenima te ima mnogo korisnih primjena. 
