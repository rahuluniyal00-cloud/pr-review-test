import boto3
import json
import os
import sys

print("Starting AI code review...")

# Read the diff
with open('pr_diff.txt', 'r') as f:
    diff_content = f.read()

if not diff_content.strip():
    print("No diff found - skipping review")
    with open('review_output.txt', 'w') as f:
        f.write("No code changes detected to review.")
    sys.exit(0)

# Truncate if too large
if len(diff_content) > 15000:
    diff_content = diff_content[:15000] + "\n\n... (diff truncated due to size)"

# Get PR info from environment variables
repo   = os.environ.get('PR_REPO', 'unknown')
title  = os.environ.get('PR_TITLE', 'unknown')
author = os.environ.get('PR_AUTHOR', 'unknown')
head   = os.environ.get('PR_HEAD', 'unknown')
base   = os.environ.get('PR_BASE', 'unknown')

print(f"Reviewing PR: {title} by {author}")

prompt = (
    "Review the following Pull Request diff and return a structured code review.\n\n"
    "## Pull Request Information\n"
    "- Repository: " + repo + "\n"
    "- PR Title: " + title + "\n"
    "- Author: " + author + "\n"
    "- Source Branch: " + head + " to " + base + "\n\n"
    "## Code Diff\n"
    + diff_content + "\n\n"
    "## Instructions\n"
    "Review this code for:\n"
    "1. Security issues - hardcoded secrets, SQL injection, missing input validation\n"
    "2. Null handling - potential NullPointerExceptions, missing null checks\n"
    "3. Code quality - magic numbers, long methods, poor naming\n"
    "4. Performance - N+1 queries, string concatenation in loops\n"
    "5. Test coverage - missing tests for new methods\n\n"
    "Respond in this exact markdown format:\n\n"
    "## AI Code Review\n\n"
    "**Overall Score: X/10**\n\n"
    "### Summary\n"
    "[2-3 sentence summary]\n\n"
    "### Critical Issues\n"
    "[List critical issues or: None found]\n\n"
    "### Warnings\n"
    "[List warnings or: None found]\n\n"
    "### Suggestions\n"
    "[List suggestions or: None found]\n\n"
    "### Category Scores\n"
    "| Category | Score | Notes |\n"
    "|----------|-------|-------|\n"
    "| Security | X/10 | ... |\n"
    "| Null Handling | X/10 | ... |\n"
    "| Code Quality | X/10 | ... |\n"
    "| Performance | X/10 | ... |\n"
    "| Test Coverage | X/10 | ... |\n\n"
    "---\n"
    "*Reviewed by Claude Sonnet 4.6 via AWS Bedrock*"
)

# Call AWS Bedrock
bedrock = boto3.client(service_name='bedrock-runtime', region_name='us-east-1')

body = json.dumps({
    "anthropic_version": "bedrock-2023-05-31",
    "max_tokens": 4096,
    "temperature": 0.1,
    "messages": [{"role": "user", "content": prompt}]
})

print("Calling AWS Bedrock (Claude Sonnet 4.6)...")

response = bedrock.invoke_model(modelId="us.anthropic.claude-sonnet-4-6", body=body)
response_body = json.loads(response['body'].read())
review_text = response_body['content'][0]['text']

print("Review received! Length: " + str(len(review_text)) + " characters")

with open('review_output.txt', 'w') as f:
    f.write(review_text)

print("Review saved successfully!")

