# Use ubuntu with OpenJDK 7
FROM java:openjdk-7-jdk

# Install boot-clj
RUN wget https://github.com/boot-clj/boot/releases/download/2.0.0-rc9/boot.sh
RUN mv boot.sh boot && chmod a+x boot && mv boot /usr/local/bin
