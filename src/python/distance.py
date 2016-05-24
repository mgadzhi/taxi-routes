#!/usr/bin/env python3

import math
import sys
import time

R = 6371.009


def main():
    distribution = {}
    
    for line in sys.stdin:
        parts = line.split(' ')
        x1 = math.radians(float(parts[2]))
        y1 = math.radians(float(parts[3]))
        x2 = math.radians(float(parts[5]))
        y2 = math.radians(float(parts[6]))

        delta_phi = x1 - x2
        delta_lambda = y1 - y2
        phi_m = (x1 + x2) / 2
        
        dist = R * math.sqrt(delta_phi ** 2 + (math.cos(phi_m) * delta_lambda) ** 2)

        #print('({}, {})\t({}, {})\t{}\t{}'.format(x1, y1, x2, y2, phi_m, dist))

        round_dist = int(dist)
        
        if round_dist not in distribution:
            distribution[round_dist] = 0
        distribution[round_dist] += 1
        #sys.stdout.write(str(round(dist)) + '\n')
    #for k in sorted(distribution.keys()):
    for k, v in distribution.items():
        sys.stdout.write('{}\t{}\n'.format(k, v))


if __name__ == '__main__':
    #start = time.time()
    main()
    #finish = time.time()
    #sys.stderr.write(str(finish - start) + '\n')
