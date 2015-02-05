# QuadPhotos
A program that splits an image into quadrants in a specific way. Inspired from https://github.com/fogleman/Quads I decided to try to make my own version to see what I can learn from it

Web Demo! http://photoquads.appspot.com/

I used a different algorithm than his, which causes my pictures to come out a little too detailed in some places, but 
the outcome looks very close to Mr. Fogleman's great work, it works well, and I am happy with it :)

![screenshot](http://i.imgur.com/SIAm64D.png)
![screenshot](http://i.imgur.com/SPakiyN.png)

I do plan on coming up with a better algorithm for determining the error ranking of each qaudrant, so more is to come!

I ended up coming up with two algorithms that both show difference animations, one which splits up the regions with the highest error first, and another that splits up all regions each iteration.
I really liked how both animations looked so I decided to keep both algorithm and have the user choose which to see.

![screenshot](http://i.imgur.com/IKyBpnD.png)

As you can see, you can change certain parameters and see the animation you want!

I even added a fun little game where you can click a region to split it up manually. If clicking is too slow for you, there's an option to split up any region you click and drag over!

![screenshot](http://i.imgur.com/qwOCDzH.png)

I always like to just reveal the head.

I loved working on this project and it taught me a lot about QuadTrees and helped me think more about
how to really tell how far off your average estimate is from an actual value (still working on that!)

