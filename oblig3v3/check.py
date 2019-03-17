#!/usr/bin/env python3

import sys
from math import sqrt
from itertools import count, islice


correct_color = ""
wrong_color = ""
color_end = ""

if sys.platform == "linux":
    correct_color = "\u001b[32m"
    wrong_color = "\u001b[31m"
    color_end = "\u001b[0m"



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
                print("{} is prime: {}{}{}".format(num, correct_color, True, color_end))
            else:
                print("{} is prime: {}{}{}".format(num, wrong_color, False, color_end))

def main():
    base = 0
    line_count = 0
    for line in sys.stdin:
        if line == '\n':
            continue
        elif "FINISH" in line or "Number" in line or "Default" in line:
            continue
        elif "correct" in line or "java" in line:
            print(line, end="")
        else:
            bits = line.split(":")
            base = int(bits[0])
            multi_bits = bits[1].split("*")
            for i in range(len(multi_bits)):
                multi_bits[i] = int(multi_bits[i])

            print("Base: {}  Factors: {}".format(base, multi_bits))

            for num in multi_bits:
                if isPrime(num):
                    print("{} is prime: {}{}{}".format(num, correct_color, True, color_end))
                else:
                    print("{} is prime: {}{}{}".format(num, wrong_color, False, color_end))

            product = 1
            line_count += 1
            for num in multi_bits:
                product *= num
            if product == base:
                print("Correct: {}{}{}".format(correct_color, product == base, color_end))
            else:
                print("Correct: {}{}{}".format(wrong_color, product == base, color_end))
            
            print("Line: {}\n".format(line_count))

if __name__ == "__main__":
    main()