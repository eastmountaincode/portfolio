PROBLEM:

In this project for the University of Cincinnati's Digital Scholarship Center, the goal was to create a text 
classifier that could, as accurately as possible, give the likelihood for a med school applicant to go into either 
(1. Family Medicine) or (2. A different medical specialization).

It was suggested that the UC Admissions Department would then use this technology to spend time and resources 
guiding applicants who are more likely to go into the Family Medicine Specialization to pursue that path.


OUTCOME

We were able to achieve an accuracy of 0.8 using a K-Neighbors Classifier on a representation of the dataset created in the following way:

A researcher working for the DSC identified vocabulary that would be likely found in family medicine specialization
essays ("compassion", "strive", "happy") and that would be likely found in other specialization essays ("trauma", "pharma", "muscle").

A feature vector was created so that each essay had the following features:

- Total sum of terms from Family Medicine vocab
- Total sum of terms from non-Family Medicine vocab
- A binary indication as to whether the essay was written by a family medicine (1) or non-family medicine specialist (0).

This was the most succesful classifier, and can be found under 04 Using Sum Of Words Associated With Either Family Medicine Or Non Family Medicine.
