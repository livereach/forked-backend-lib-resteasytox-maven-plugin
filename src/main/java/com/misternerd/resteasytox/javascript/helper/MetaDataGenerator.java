package com.misternerd.resteasytox.javascript.helper;

import com.misternerd.resteasytox.Metadata;
import org.apache.commons.io.IOUtils;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MetaDataGenerator
{

	private final Path outputPath;

	private final MavenProject project;

	private final Metadata metadata;


	public MetaDataGenerator(Path outputPath, MavenProject project, Metadata metadata)
	{
		this.outputPath = outputPath;
		this.project = project;
		this.metadata = metadata;
	}


	public void createFiles() throws IOException
	{
		createNpmPackageFile();
	}


	private void createNpmPackageFile() throws IOException
	{
		String content = loadNpmPackageFileTemplate();
		content = replacePlaceholders(content);
		persistNpmPackageFile(content);
	}


	private String loadNpmPackageFileTemplate() throws IOException
	{
		InputStream stream = getClass().getClassLoader().getResourceAsStream("js_package_template.json");
		return IOUtils.toString(stream);
	}

	private String replacePlaceholders(String content) {
		return content
			.replace("##NAME##", metadata.getName())
			.replace("##VERSION##", project.getVersion())
			.replace("##DESCRIPTION##", metadata.getDescription())
			.replace("##AUTHOR##", metadata.getAuthor())
			.replace("##HOMEPAGE##", metadata.getHomepage())
			.replace("##SCMURL##", metadata.getScmUrl())
			.replace("##EMAIL##", metadata.getEmail());
	}

	private void persistNpmPackageFile(String content) throws IOException
	{
		Path target = outputPath.resolve("package.json");
		Files.write(target, content.getBytes(StandardCharsets.UTF_8));
	}

}