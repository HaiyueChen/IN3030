#!/usr/bin/env python

import sys
from math import sqrt
from itertools import count, islice


def isPrime(n):
    return n > 1 and all(n%i for i in islice(count(2), int(sqrt(n)-1)))

def check_list_prime():
    for line in sys.stdin:
        line = line.strip('[')
        line = line.split(']')[0]
        line = line.strip('\n')
        bits = line.split(',')
        for i in range(len(bits)):
            bits[i] = int(bits[i])
        for num in bits:
            if (isPrime(num)):
                print("{} is prime: \u001b[32m{}\u001b[0m".format(num, True))
            else:
                print("{} is prime: \u001b[31m{}\u001b[0m".format(num, False))


def main():
    base = 0
    line_count = 0
    for line in sys.stdin:
        if line == "\n":
            continue
        else:
            if "Factoring" in line:
                bits = line.split(":")
                base = int(bits[1])
            else:
                bits = line.split(" x ")
                for i in range(len(bits)):
                    bits[i] = int(bits[i])
                print("Base: ", base, "Factors {}".format(bits))
                
                for num in bits:
                    if not isPrime(num):
                        print("{} is prime: \u001b[31m{}\u001b[0m".format(num, isPrime(num)))
                    else:
                        print("{} is prime: \u001b[32m{}\u001b[0m".format(num, isPrime(num)))

                product = 1
                line_count += 1
                for num in bits:
                    product *= num
                if product != base:
                    print("Base: ", base, "Factors {}".format(bits), "\nCorrect: \u001b[31m{}\u001b[0m".format(product == base))
                else:
                    print("Base: ", base, "Factors {}".format(bits), "\nCorrect: \u001b[32m{}\u001b[0m".format(product == base))
                print("Line: {}\n".format(line_count))

if __name__ == "__main__":
    # main()

    # numbers = [0, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97]
    # for i in numbers:
    #     print(i,  isPrime(i))

    check_list_prime()