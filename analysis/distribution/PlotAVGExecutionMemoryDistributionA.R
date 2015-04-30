#Plot the memory model, first argument is the upperbound, the the dataset containing the plotting data

args<-commandArgs(TRUE)
lowerbound <-  as.integer(args[1])
upperbound <-  as.integer(args[2])
dataset <-read.csv(args[3])
filename = gsub(".csv", "", args[3])
fileNameEt <- paste(filename, "_FREQUENCE", sep="")
graphtitle1 <- paste(paste("RESULT EN0-EN9 ", lowerbound), upperbound)
graphtitle2 <- "FREQ AVG RESULT "
breaks <- c(11,116.2,221.4,326.6,431.8,537,642.2,747.4,852.6,957.8,1063)
breaksSS <- c(16.4,113.22,210.04,306.86,403.68,500.5,597.32,694.14,790.96,887.78,984.6)

print(fileNameEt)

xmean <- rowMeans(cbind(dataset$MEMA0,dataset$MEMA1 ,dataset$MEMA2 ,dataset$MEMA3 ,dataset$MEMA4,dataset$MEMA5,dataset$MEMA6,dataset$MEMA7,dataset$MEMA8,dataset$MEMA9))

png(paste(fileNameEt,"_FULL_DENSITY_A.png") , 1827, 1458)
d<- density(xmean)
plot(d, main = "Density" ,col="brown", type="h", ylim=c(0,0.05), xlim=range(breaks))


png(paste(fileNameEt,"_STEADY_DENSITY_A.png") , 1827, 1458)
d<- density(xmean[lowerbound:upperbound])
plot(d, main = "Density Steady" , col="brown", type="h", ylim=c(0,0.05), xlim=range(breaksSS))

png(paste(fileNameEt,"_FULL_HIST_A.png") , 1827, 1458)
hist(xmean, main="Histogram", col="brown", xlim=range(breaks), ylim=c(0,20000), labels=TRUE, breaks=breaks)

png(paste(fileNameEt,"_STEADY_HIST_A.png") , 1827, 1458)
hist(main="Histogram Steady", col="brown",xmean[lowerbound:upperbound], xlim=range(breaksSS), ylim=c(0,5000), labels=TRUE, breaks=breaksSS)


png(paste(fileNameEt,"_SUMMARY_A.png") , 1827, 1458)
split.screen(c(2,1))
split.screen(c(1,2), screen=1)
screen(3)
d<- density(xmean)
plot(d, main = "Density" , col="brown", type="h", ylim=c(0,0.05), xlim=range(breaks))
screen(4)
d<- density(xmean[lowerbound:upperbound])
plot(d, main = "Density Steady" ,col="brown", type="h", ylim=c(0,0.05), xlim=range(breaksSS))
screen(2)
split.screen(c(1,2), screen=2)
hist(xmean, main="Histogram", col="brown", xlim=range(breaks), ylim=c(0,20000), labels=TRUE, breaks=breaks)
screen(6)
hist(main="Histogram Steady",col="brown", xmean[lowerbound:upperbound], xlim=range(breaksSS), ylim=c(0,5000), labels=TRUE, breaks=breaksSS)

dev.off()






