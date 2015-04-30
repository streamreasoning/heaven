#Plot the memory model, first argument is the upperbound, the the dataset containing the plotting data
args<-commandArgs(TRUE)
lowerbound <-  as.integer(args[1])
upperbound <-  as.integer(args[2])
dataset <- read.csv(args[3])
filename = gsub(".csv", "", args[3])
fileNameEt <- paste(filename, "_FREQUENCE.png", sep="")
graphtitle1 <- paste(paste("RESULT EN0-EN9 ", lowerbound), upperbound)
graphtitle2 <- "FREQ AVG RESULT "
png(paste(fileNameEt), 1827, 1458)
xmean <- rowMeans(cbind(dataset$MEMB0,dataset$MEMB1 ,dataset$MEMB2 ,dataset$MEMB3 ,dataset$MEMB4,dataset$MEMB5,dataset$MEMB6,dataset$MEMB7,dataset$MEMB8,dataset$MEMB9))


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
hist(xmean, main="Histogram", labels=TRUE, breaks=10)
screen(6)
hist(main="Histogram Steady", xmean[lowerbound:upperbound], labels=TRUE, breaks=10)


dev.off()

