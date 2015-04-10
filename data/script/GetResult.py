# -*- coding: utf-8 -*-
"""
Created on Fri Mar 20 09:34:34 2015

@author: hieutran
"""


with open('/Volumes/DATA/Project/Java/SBR/data/Microsoft-Baseline.txt') as f:
   lines = f.readlines()

UNI = 0
BIG = 0
LDA = 0

for index,line in enumerate(lines):
    if "Accuracy" in line:
        if "UNI" in lines[index+1]:
            arrays = line.split()
            percent = arrays[2].split('%')
            UNI += float(percent[0])
            
        if "BIG" in lines[index+1]:
            arrays = line.split()
            percent = arrays[2].split('%')
            BIG += float(percent[0])
        
        if "LDA" in lines[index+1]:
            arrays = line.split()
            percent = arrays[2].split('%')
            LDA += float(percent[0])
            
print "UNI:" + str(UNI/10)
print "BIG:" + str(BIG/10)
print "LDA:" + str(LDA/10)
            