#Plot the memory model, first argument is the upperbound, the the dataset containing the plotting data
#Load Data 
args<-commandArgs(TRUE)
upperbound <- as.integer(args[1])
dataset <- read.csv(args[2])
filename = gsub(".csv", "", args[2])
fileNameEt <- paste(filename, "_AVG_B.png", sep="")
graphtitle1 <- "MEMORY BEFORE RESULT EN0-EN9"
graphtitle2 <- "MEMORY BEFORE AVG RESULT "
png(paste(fileNameEt), 1827, 1458)
png(paste(filename, "_AVG_B_THUMBNAIL.png"), 150, 121)

#upperbound <- max(cbind(dataset$MEMB0,dataset$MEMB1 ,dataset$MEMB2 ,dataset$MEMB3 ,dataset$MEMB4,dataset$MEMB5,dataset$MEMB6,dataset$MEMB7,dataset$MEMB8,dataset$MEMB9))
xmean <- rowMeans(cbind(dataset$MEMB0,dataset$MEMB1 ,dataset$MEMB2 ,dataset$MEMB3 ,dataset$MEMB4,dataset$MEMB5,dataset$MEMB6,dataset$MEMB7,dataset$MEMB8,dataset$MEMB9))
plotsummary = summary(xmean)

#Plot the AVG
plot(xmean ~ dataset$ENUM, type="l", col="brown", ylab = "memory B", xlab = "Events", yaxt="n", ylim=c(0,upperbound), ,axes=FALSE)
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
for(j in seq(1,6,1)){
  text(800, upperbound -(40*(j-1)), (paste(names(plotsummary)[j],as.vector(plotsummary)[j])) , cex = 1, adj = 0)
}
text(300, upperbound - 55, paste("Stationary Mean",mean(xmean[200:999])) , cex = 1, adj = 0)

title(graphtitle1,filename)


dev.off()


