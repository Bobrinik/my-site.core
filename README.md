# my-site.core

This project builds my one page GitHub website portfolio.

## Overview

- I didn't want to update my GitHub website HTML every time I add a new project. So I wrote some code to handle all of the rendering and updating. 
- I specify list of projects that I want to add in `projects.clj`. ClojureScript handles the rest.
- Note that in order for this to work projects specified in `projects.clj` have to be in my repository and each of them has to have a `PROJECT.md` file.

## Development

To get an interactive development environment run:

    lein fig:build

This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

	lein clean

To create a production build run:

	lein clean
	lein fig:min
