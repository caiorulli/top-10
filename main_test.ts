import { assert, assertEquals } from "@std/assert";
import { add, init, baseValue, nextEntries, applyMatch, Ratings, topList } from "./main.ts";

Deno.test(function addTest() {
  assertEquals(add(2, 3), 5);
});

const exampleCandidates = [
  "Iliad",
  "Odissey",
  "Aeneid",
  "Brothers Karamazov",
  "Father Goriot",
  "Fictions",
  "The Old man and the Sea",
  "Dune",
  "The Lord of the Rings",
  "Dracula",
  "1Q84"
]

Deno.test(function testInitRatings_empty() {
  assertEquals(init([]), {})
})

Deno.test(function testInitRatings_standard() {
  const ratings = init(exampleCandidates)
  const keys = Object.keys(ratings)
  assertEquals(keys, exampleCandidates)

  const values = Object.values(ratings)
  assert(values.every(c => c === baseValue))
});

Deno.test(function testNextEntries() {
  const ratings = init(exampleCandidates)
  const [a, b] = nextEntries(ratings)
  assert(a != b)
  assert(exampleCandidates.some(c => c === a))
  assert(exampleCandidates.some(c => c === b))
});

Deno.test(function testApplyMatch() {
  const ratings = init(exampleCandidates)
  const [a, b] = nextEntries(ratings)
  const newRatings = applyMatch(ratings, a, b)
  assert(newRatings[a] > baseValue)
  assert(newRatings[a] > newRatings[b])
  assert(baseValue > newRatings[b])
})

function executeMatches(ratings: Ratings, matches: [string, string][]): Ratings {
  return matches.reduce((currentRatings, [winner, loser]) => 
    applyMatch(currentRatings, winner, loser),
    ratings)
}

Deno.test(function testTopList() {
  const ratings = executeMatches(
    init(exampleCandidates),
    [
      ["Brothers Karamazov", "Father Goriot"],
      ["Iliad", "Odissey"],
      ["Iliad", "Aeneid"],
      ["Odissey", "Aeneid"],
      ["The Lord of the Rings", "Dracula"],
      ["1Q84", "Father Goriot"],
      ["Fictions", "The Old man and the Sea"]
    ])
  
  assertEquals(topList(ratings), [
    "Iliad",
    "Brothers Karamazov",
    "Fictions",
    "The Lord of the Rings",
    "1Q84",
  ])
})