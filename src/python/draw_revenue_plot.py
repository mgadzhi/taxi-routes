#!/usr/bin/env python2

import datetime
import matplotlib.dates as mdates
import matplotlib.pyplot as plt
import numpy as np
import sys


def parse_line(line):
    dt, r = line.split('\t')
    return datetime.datetime.strptime(dt, '%Y-%m-%d %H:%M:%S'), float(r)


def main():
    #fname = sys.argv[1]
    hours, revenues = np.loadtxt("all.revenue",
                                 delimiter='\t',
                                 unpack=True,
                                 converters={0: mdates.strpdate2num('%Y-%m-%d %H:%M:%S')})
    plt.bar(hours, revenues)
    plt.title("Hour")
    plt.ylabel("Revenue")
    plt.grid(True)
    plt.show()


if __name__ == '__main__':
    main()
