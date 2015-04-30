#Plot the memory model, first argument is the upperbound, the the dataset containing the plotting data
#Load Data 
args<-commandArgs(TRUE)
upperbound <- as.integer(args[1])
dataset <- read.csv(args[2])
filename = gsub(".csv", "", args[2])
fileNameEt <- paste(filename, "_AVG_A.png", sep="")
graphtitle1 <- "MEMORY AFTER RESULT EN0-EN4"
graphtitle2 <- "MEMORY AFTER AVG RESULT "
png(paste(fileNameEt), 1827, 1458)
png(paste(filename, "_AVG_A_THUMBNAIL.png"), 150, 121)

#upperbound <- max(cbind(dataset$MEMA0,dataset$MEMA1 ,dataset$MEMA2 ,dataset$MEMA3 ,dataset$MEMA4,dataset$MEMA5,dataset$MEMA6,dataset$MEMA7,dataset$MEMA8,dataset$MEMA9))
xmean <- rowMeans(cbind(dataset$MEMA0,dataset$MEMA1 ,dataset$MEMA2 ,dataset$MEMA3 ,dataset$MEMA4,dataset$MEMA5,dataset$MEMA6,dataset$MEMA7,dataset$MEMA8,dataset$MEMA9))
plotsummary = summary(xmean)

#Plot the AVG
plot(xmean ~ dataset$ENUM, type="l", col="brown", ylab = "memory A", xlab = "Events", yaxt="n", ylim=c(0,upperbound), ,axes=FALSE)
par(new=TRUE)

abline(h = mean(xmean), v = 0 , col = "gray60")
par(new=TRUE)
abline(h = mean(xmean[200:999]), v = 0, col = "red2")
par(new=TRUE)

#Better Axis
axis(1, at = seq(min(dataset$ENUM), max(dataset$ENUM)+1, by = 25))
axis(2, at = seq(0,  10 + (upperbound - upperbound %% 10), by = 10))


#Grid Behind
grid(nx = NULL, ny = NULL, col = "gray1", lty = "dotted",lwd = par("lwd"), equilogs = TRUE)

#Usefult informations
text(200, upperbound - 250, paste("StatMean",mean(xmean[200:2500])) , cex = 0.8, adj = 0)
for(j in seq(1,6,1)){
  text(200, upperbound -(40*(j-1)), (paste(names(plotsummary)[j],as.vector(plotsummary)[j])) , cex = 0.8, adj = 0)
}


title(graphtitle1,filename)


dev.off()

