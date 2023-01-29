FROM gradle:7.4.0-jdk17

RUN gradle installDist

CMD ./build/bootScripts/java-project-73