#Plot the memory model, first argument is the upperbound, the the dataset containing the plotting data

args<-commandArgs(TRUE)
dataset <-read.csv(args[1])
filename = gsub(".csv", "", args[1])
fileNameEt <- paste(filename, "_FREQUENCE", sep="")
breaks   <- c(11,117,223,329,435,541,647,753,859,965,1071)
breaksSS <- c(16.4,111.13,205.86,300.59,395.32,490.05,584.78,679.51,774.24,868.97,963.7)

xmean <- rowMeans(cbind(dataset$MEMA0,dataset$MEMA1 ,dataset$MEMA2 ,dataset$MEMA3 ,dataset$MEMA4,dataset$MEMA5,dataset$MEMA6,dataset$MEMA7,dataset$MEMA8,dataset$MEMA9))


png(paste(fileNameEt,"_FULL_DENSITY_B.png") , 1827, 1458)
d<- density(xmean)
plot(d, main = "Density", col="brown", type="h", ylim=c(0,0.05), xlim=range(breaks))

png(paste(fileNameEt,"_FULL_HIST_B.png") , 1827, 1458)
hist(xmean, main="Histogram",  col="brown", xlim=range(breaks), ylim=c(0,20000), labels=TRUE, breaks=breaks)

dev.off()
