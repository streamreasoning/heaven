#Plot the latency model, first argument is the upperbound, the the dataset containing the plotting data
#Load Data 
args<-commandArgs(TRUE)
upperbound <- as.integer(args[1])
dataset <- read.csv(args[2])
filename = gsub(".csv", "", args[2])
fileNameEt <- paste(filename, "_AVG.png", sep="")
graphtitle1 <- "LATENCY RESULT EN0-EN4"
graphtitle2 <- "LATENCY AVG RESULT "
png(paste(fileNameEt), 1827, 1458)
#png(paste(filename, "_AVG_THUMBNAIL.png", sep=""), 250, 200)
upperbound2 <- max(cbind(dataset$LAT0,dataset$LAT1 ,dataset$LAT2 ,dataset$LAT3 ,dataset$LAT4,dataset$LAT5,dataset$LAT6,dataset$LAT7,dataset$LAT8,dataset$LAT9))
xmean <- rowMeans(cbind(dataset$LAT0,dataset$LAT1 ,dataset$LAT2 ,dataset$LAT3 ,dataset$LAT4,dataset$LAT5,dataset$LAT6,dataset$LAT7,dataset$LAT8,dataset$LAT9))
plotsummary = summary(xmean)


#Plot the AVG
plot(xmean ~ dataset$ENUM, type="l", col="brown", ylab = "latency", xlab = "Events", yaxt="n", ylim=c(0,upperbound), ,axes=FALSE)
par(new=TRUE)

abline(h = mean(xmean), v = 0 , col = "gray60")
par(new=TRUE)
abline(h = mean(xmean[200:999]), v = 0, col = "blue")
par(new=TRUE)

#Better Axis
axis(1, at = seq(min(dataset$ENUM), max(dataset$ENUM)+1, by = 25))
axis(2, at = seq(0,  10 + (upperbound - upperbound %% 10), by = 10))


#Grid Behind
grid(nx = NULL, ny = NULL, col = "gray1", lty = "dotted",lwd = par("lwd"), equilogs = TRUE)

#Usefult informations
for(j in seq(1,6,1)){
  text(800, upperbound -(10*(j-1)), (paste(names(plotsummary)[j],as.vector(plotsummary)[j])) , cex = 0.7, adj = 0)
}
  text(800, upperbound - 60, paste("Stationary Mean",mean(xmean[200:2500])) , cex = 0.7, adj = 0)


title(graphtitle1,filename)


dev.off()
