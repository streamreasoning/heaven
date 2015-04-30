#Plot the memory model, first argument is the upperbound, the the dataset containing the plotting data

args<-commandArgs(TRUE)
lowerbound <-  as.integer(args[1])
upperbound <-  as.integer(args[2])
dataset <-read.csv(args[3])
filename = gsub(".csv", "", args[3])
fileNameEt <- paste(filename, "_FREQUENCE", sep="")
graphtitle1 <- paste(paste("RESULT EN0-EN9 ", lowerbound), upperbound)
graphtitle2 <- "FREQ AVG RESULT "
breaks   <- c(11,117,223,329,435,541,647,753,859,965,1071)
breaksSS <- c(16.4,111.13,205.86,300.59,395.32,490.05,584.78,679.51,774.24,868.97,963.7)

xmean <- rowMeans(cbind(dataset$MEMA0,dataset$MEMA1 ,dataset$MEMA2 ,dataset$MEMA3 ,dataset$MEMA4,dataset$MEMA5,dataset$MEMA6,dataset$MEMA7,dataset$MEMA8,dataset$MEMA9))


png(paste(fileNameEt,"_FULL_DENSITY_B.png") , 1827, 1458)
d<- density(xmean)
plot(d, main = "Density" , col="brown", type="h",ylim=c(0,0.05), xlim=range(breaks))

png(paste(fileNameEt,"_STEADY_DENSITY_B.png") , 1827, 1458)
d<- density(xmean[lowerbound:upperbound])
plot(d, main = "Density Steady" ,col="brown",type="h", ylim=c(0,0.05), xlim=range(breaksSS))

png(paste(fileNameEt,"_FULL_HIST_B.png") , 1827, 1458)
hist(xmean, main="Histogram",  col="brown",xlim=range(breaks), ylim=c(0,20000), labels=TRUE, breaks=breaks)

png(paste(fileNameEt,"_STEADY_HIST_B.png") , 1827, 1458)
hist(main="Histogram Steady", col="brown",xmean[lowerbound:upperbound], xlim=range(breaksSS), ylim=c(0,5000), labels=TRUE, breaks=breaksSS)


png(paste(fileNameEt,"_SUMMARY_B.png") , 1827, 1458)
split.screen(c(2,1))
split.screen(c(1,2), screen=1)
screen(3)
d<- density(xmean)
plot(d, main = "Density" , col="brown", type="h",ylim=c(0,0.05), xlim=range(breaks))
screen(4)
d<- density(xmean[lowerbound:upperbound])
plot(d, main = "Density Steady" ,col="brown", type="h", ylim=c(0,0.05), xlim=range(breaksSS))
screen(2)
split.screen(c(1,2), screen=2)
hist(xmean, main="Histogram",  col="brown",xlim=range(breaks), ylim=c(0,20000), labels=TRUE, breaks=breaks)
screen(6)
hist(main="Histogram Steady", col="brown",xmean[lowerbound:upperbound], xlim=range(breaksSS), ylim=c(0,5000), labels=TRUE, breaks=breaksSS)

dev.off()
