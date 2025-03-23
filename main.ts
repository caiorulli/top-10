export function add(a: number, b: number): number {
  return a + b
}

export const baseValue = 1000
const k = 32

export type Ratings = { [key: string]: number }

export function init(candidates: string[]) : Ratings {
  return candidates.reduce((acc, candidate) => 
    ({...acc, [candidate]: baseValue}), {})
}

function getRandomElement<T>(collection: T[]): T {
  return collection[Math.floor(Math.random() * collection.length)];
}

export function nextEntries(ratings: Ratings): [ string, string ] {
  const candidates = Object.keys(ratings)

  const a = getRandomElement(candidates)
  let b = a;
  while (b == a) {
    b = getRandomElement(candidates)
  }
  return [ a, b ]
}

export function applyMatch(ratings: Ratings, winner: string, loser: string): Ratings {
  const pWinner = Math.pow(10, (ratings[winner] / 400))
  const pLoser = Math.pow(10, (ratings[loser] / 400))
  const pTotal = pWinner + pLoser

  const winnerPoints = ratings[winner] + k * (1 - pWinner / pTotal)
  const loserPoints = ratings[loser] -  k * pLoser / pTotal

  return {...ratings, [winner]: winnerPoints, [loser]: loserPoints}
}

export function topList(ratings: Ratings): string[] {
  const entries = Object.entries(ratings)
  return entries.sort(([_aKey, a], [_bKey, b]) => b - a)
    .map(([key]) => key)
    .slice(0, 5)
}

// Learn more at https://docs.deno.com/runtime/manual/examples/module_metadata#concepts
if (import.meta.main) {
  console.log("Add 2 + 3 =", add(2, 3))
}