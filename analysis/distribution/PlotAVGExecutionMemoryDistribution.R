<!--  -->#Plot the memory model, first argument is the upperbound, the the dataset containing the plotting data

args<-commandArgs(TRUE)
lowerbound <-  15000
upperbound <-  20000
dataset <- UFD_MEMLOG_EN21_2015_04_30_BIG_SHUFFLED_RHODF_NAIVE_STMT_FRP_K1_EW_10000
filename = "UFD_MEMLOG_EN21_2015_04_30_BIG_SHUFFLED_RHODF_NAIVE_STMT_FRP_K1_EW_10000"
fileNameEt <- paste(filename, "_FREQUENCE_A.png", sep="")
graphtitle1 <- paste(paste("RESULT EN0-EN9 ", lowerbound), upperbound)
graphtitle2 <- "FREQ AVG RESULT "
breaks <- c(11,116.2,221.4,326.6,431.8,537,642.2,747.4,852.6,957.8, 1063)
breaksSS <- c(16.4,113.22,218.42,323.62,428.82,534.02,639.22,744.42,849.62,954.82, 1060.02)

print(fileNameEt)
png(fileNameEt, 1827, 1458)
xmean <- rowMeans(cbind(dataset$MEMA0,dataset$MEMA1 ,dataset$MEMA2 ,dataset$MEMA3 ,dataset$MEMA4,dataset$MEMA5,dataset$MEMA6,dataset$MEMA7,dataset$MEMA8,dataset$MEMA9))


#Plot first Graph: All the dataset for 5 experiments

#______ Window With 2 Rows, Top Row In 2 Columns ______#
split.screen(c(2,1)) # Makes Screen 1 and 2
split.screen(c(1,2), screen=1) # Makes Screen 3 and 4
screen(3)
d<- density(xmean)
plot(d, main = "Density" )
screen(4)
d<- density(xmean[lowerbound:upperbound])
plot(d, main = "Density Steady" )
screen(2)
split.screen(c(1,2), screen=2)
hist(xmean, main="Histogram", freq=TRUE, xlim=range(breaks), ylim=c(0,20000), labels=TRUE, breaks=breaks)
screen(6)
hist(main="Histogram Steady", xmean[lowerbound:upperbound], xlim=range(breaksSS), ylim=c(0,5000), labels=TRUE, breaks=breaksSS)
