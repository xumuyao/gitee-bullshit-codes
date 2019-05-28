using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace E {
    class Program {
        static void Main (string[] args) {

            var numArray = new int[] { 5, 2, 9, 1 };

            var sortThreads = new List<Thread> ();

            foreach (var item in numArray) {
                var newThread = new Thread ((data) => {
                    Thread.Sleep (item * 1000);
                    Console.WriteLine (item);
                });
                sortThreads.Add (newThread);
            }

            foreach (var item in sortThreads) {
                item.Start ();
            }

            Console.ReadKey ();
        }

    }
}