#!/usr/bin/env python3

import math
import sys

R = 6371.009


def main():
    distribution = {}
    
    for line in sys.stdin:
        parts = line.split(' ')
        x1 = math.radians(float(parts[2]))
        y1 = math.radians(float(parts[3]))
        x2 = math.radians(float(parts[5]))
        y2 = math.radians(float(parts[6]))

        delta_phi = x2 - x1
        delta_lambda = y2 - y1
        phi_m = (x1 + x2) / 2
        
        dist = R * math.sqrt(delta_phi ** 2 + (math.cos(phi_m) * delta_lambda) ** 2)

        round_dist = math.ceil(dist)
        
        if round_dist not in distribution:
            distribution[round_dist] = 0
        distribution[round_dist] += 1

    for k, v in distribution.items():
        sys.stdout.write('{}\t{}\n'.format(k, v))


if __name__ == '__main__':
    main()
