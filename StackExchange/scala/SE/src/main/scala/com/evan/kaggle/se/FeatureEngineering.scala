package com.evan.kaggle.se

import edu.stanford.nlp.simple.Sentence
import edu.stanford.nlp.simple.Document
import collection.JavaConverters._
import scala.collection.immutable.HashSet

object FeatureEngineering
{
	/* Make n-grams from input list */
	/* TODO: use iterators to save conversion costs*/
	def mkNgram[A](n: Int, in: List[A]): List[List[A]] =
	{
		in.sliding(n).map(_.toList).toList
	}

	/* LPDU is just a (lemma, hasUpper), dependency, part of speech, isUpper quad */
	case class LPDU(l: String, p: String, d: String, u:Boolean)

	/* Maps a sentence to a list of LPDU triples */
	def mkLPDUlist(s: Sentence): List[LPDU] =
	{
		val l = s.lemmas
		val p = s.posTags
		val d = s.incomingDependencyLabels.asScala.map(x => if (x.isPresent) x.get.toString else "")
		val u = s.words.asScala.map(_.exists(_.isUpper))

		/* so imperative :( :( */
		(for (i <- 0 until l.size if d(i) != "punct")
			yield LPDU(l.get(i), p.get(i), d(i), u(i))) toList
	}

	/* Simple struct representing training features */
	case class TrainingFeatures(nGram: String, posTags: String, depTags: String, relPos: Double,
								numWords: Int, hasUpper: Boolean, isTitle: Boolean, isTag: Boolean)

	/* Simple struct representing std features */
	case class StdFeatures(nGram: String, posTags: String, depTags: String, relPos: Double,
						   numWords: Int, hasUpper: Boolean, isTitle: Boolean)

	/* Creates a TrainingFeatures instance from a StdFeatures instance */
	def stdFeat2TrFeat(s: StdFeatures, isTag: Boolean): TrainingFeatures =
		TrainingFeatures(s.nGram, s.posTags, s.depTags, s.relPos, s.numWords,
			s.hasUpper, s.isTitle, isTag)

	def makeTrFeatures(n: Int)(title: String, content: String, tags: String): Seq[TrainingFeatures] =
	{
		val lemmaTagSet: HashSet[String] = HashSet(tags.split(" ").map(tag => 
			(new Sentence(tag.replace("-", " "))).lemmas.asScala.mkString("-")):_*)
		makeStdFeatures(n)(title, content).map(stdFeat => {
				stdFeat2TrFeat(stdFeat, lemmaTagSet.contains(stdFeat.nGram))
			})
	}

	def makeStdFeatures(n: Int)(title: String, content: String): Seq[StdFeatures] =
	{
		// mk post sentence
		val post_sents = (new Document(content)).sentences.asScala.toList

		// mk title sentence
		val title_sents = (new Document(title)).sentences.asScala.toList

		// gather lemmas, pos tags, deps
		// these features will be also be ngrammed
		val post_feats: List[(List[LPDU], Boolean)] = post_sents.map(x => (mkLPDUlist(x), false))
		val title_feats: List[(List[LPDU], Boolean)] = title_sents.map(x => (mkLPDUlist(x), true))

		/* N-Grammable features (plus isTitle)*/
		val gram_feats_it: List[(List[LPDU], Boolean)] = post_feats ++ title_feats

		/* Loop over all ngram lengths */
		(1 to n).flatMap{nGramLen => {
			/* loop over all sentences in this post+title */
			gram_feats_it.flatMap{ case (n_grammable: List[LPDU], isTitle: Boolean) => {
				/* make n grams from n-grammable items */
				val ngrams: List[List[LPDU]] = mkNgram(nGramLen, n_grammable)

				/* sentence length is the lenth of n_grammable*/
				val senLen: Double = n_grammable.length.toDouble

				/* TODO: apply filters */
				// val ngrams_good = ngramFilter(ngrams)

				/* Map ngrams to StdFeature */
				ngrams.zipWithIndex.map{case (ngram: List[LPDU], index: Int) => {
					/* Now we can calculate all of the std features: */
					val lemmas = new StringBuffer()
					val posTags = new StringBuffer()
					val depTags = new StringBuffer()
					var hasUpper = false

					for (i <- 0 until ngram.length)
					{
						val lpdu = ngram(i)
						lemmas.append(lpdu.l)
						posTags.append(lpdu.p)
						depTags.append(lpdu.d)
						/* middle dependent "-" marks */
						if (i < ngram.length - 1)
						{
							lemmas.append("-")
							posTags.append("-")
							depTags.append("-")
						}

						/* check if any upper case */
						hasUpper ||= lpdu.u
					}
					/* calc rest of feats */
					val relPos = index / senLen
					val numWords = nGramLen
					StdFeatures(lemmas.toString, posTags.toString, depTags.toString,
						relPos, numWords, hasUpper, isTitle)
				}}
			}
			}}}
	}
}